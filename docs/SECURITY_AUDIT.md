# Auditoria de segurança — StudyMind Redação

Data da análise: 2026-10-02  
Escopo: repositório backend `studymindredacao`, estado atual da árvore de trabalho e histórico Git alcançável por `--all`.  
Método: análise estática e comandos locais somente leitura. O frontend não está neste repositório. Não foram feitas requisições externas, execução contra produção, alteração de código, instalação de dependências ou commit.

## 1. Resumo

Não recomendo expor o backend diretamente à internet, mesmo para dois testadores, antes de corrigir SEC-01 e SEC-02. O cadastro público permite criar contas ilimitadas, e cada conta recebe 10 correções diárias pagas; login e cadastro também não têm limitação por IP/conta. Não encontrei segredo real no estado atual nem no histórico Git alcançável, e os controles de JWT, isolamento entre usuários, validação das notas da IA e reserva atômica da cota estão bem implementados. Os demais achados são endurecimentos importantes, mas podem ser tratados depois dos dois bloqueadores se o acesso ao ambiente ficar restrito.

## 2. Tabela de achados

| ID | Severidade | Título | Arquivo:linha | Bloqueia os testes? (sim/não) |
|---|---|---|---|---|
| SEC-01 | Alta | Cadastro público permite multiplicar a cota paga por contas ilimitadas | `SecurityConfig.java:38`; `AuthController.java:28`; `application.properties:28` | sim |
| SEC-02 | Alta | Login e cadastro não têm rate limit por IP ou conta | `SecurityConfig.java:38`; `AuthController.java:28-37` | sim |
| SEC-03 | Média | Limites de campo não limitam o corpo HTTP antes da desserialização | `LoginRequest.java:6-8`; `application.properties` | não |
| SEC-04 | Média | JWT é válido por 120 minutos e não há revogação no servidor | `JwtTokenAdapter.java:63-80`; `SecurityConfig.java:35`; `application.properties:25` | não |
| SEC-05 | Média | Texto não confiável compartilha o mesmo prompt com instruções; defesa contra prompt injection é apenas textual | `PromptAvaliacaoBuilder.java:27-42`; `avaliacao-v2.txt:3-4` | não |
| SEC-06 | Baixa | Mensagens de falha externas ou influenciáveis entram nos logs sem normalização | `GeminiAvaliacaoAdapter.java:78-87`; `EnviarRedacaoService.java:83-86` | não |
| SEC-07 | Baixa | `.env` e configurações locais sensíveis não estão cobertos pelo `.gitignore` | `.gitignore:1-28` | não |
| SEC-08 | Baixa | Resposta bruta do Gemini é armazenada sem política de retenção visível | `GeminiRespostaParser.java:67`; `V1__create_table_usuarios_perfils_redacoes_e_avaliacoes.sql:45` | não |
| SEC-09 | Informativa | HTTPS/proxy, limite de requisição e versões vulneráveis dependem do ambiente de deploy | `application.properties:1-30`; `pom.xml:5-66` | não |

## 3. Detalhe dos achados

### SEC-01 — Cadastro público permite multiplicar a cota paga por contas ilimitadas

**Evidência**

```java
.requestMatchers(HttpMethod.POST, "/auth/cadastro", "/auth/login").permitAll()
```

```properties
app.limites.correcoes-diarias-free=${LIMITE_CORRECOES_DIARIAS:10}
```

Não há convite, allowlist, verificação de e-mail, CAPTCHA ou limite global de gasto. `CadastrarUsuarioService` sempre cria `Role.ALUNO` e uma conta nova recebe sua própria cota.

**Cenário concreto:** após descobrir a URL pública, uma pessoa automatiza `POST /auth/cadastro`, cria muitas contas com e-mails distintos e envia dez redações por conta. O limite por usuário funciona, mas não contém o custo agregado no Gemini.

**Impacto:** consumo de cota/custo e possível indisponibilidade para os dois testadores.  
**Recomendação:** para o teste fechado, desabilitar cadastro público e pré-criar as duas contas, ou exigir convite de uso único. Como defesa adicional, impor teto global diário/mensal de chamadas/custo, independente da conta.  
**Esforço:** P para fechar o cadastro no teste; M para convites e orçamento global.

### SEC-02 — Login e cadastro não têm rate limit por IP ou conta

**Evidência**

Os dois endpoints públicos chamam diretamente os casos de uso; não existe filtro/interceptor/biblioteca de rate limit nem contador por IP ou identidade.

```java
@PostMapping("/cadastro")
public UsuarioResponse cadastrar(...)

@PostMapping("/login")
public TokenResponse login(...)
```

**Cenário concreto:** um atacante tenta senhas repetidamente contra um e-mail conhecido ou dispara cadastros em volume. Cada tentativa de senha existente executa BCrypt, o que também amplia o potencial de consumo de CPU.

**Impacto:** tomada de conta se a senha for fraca, criação em massa de contas e degradação do serviço.  
**Recomendação:** limitar por IP no proxy e na aplicação; no login, combinar IP + identificador normalizado, janela curta, atraso progressivo e resposta uniforme. No cadastro, usar limite mais restrito e o controle de convite do SEC-01. Preservar corretamente o IP original somente de proxies confiáveis.  
**Esforço:** M.

### SEC-03 — Limites de campo não limitam o corpo HTTP antes da desserialização

**Evidência**

`EnviarRedacaoRequest.texto` tem `@Size(max = 5000)`, mas essa validação ocorre depois que o JSON foi recebido e convertido. `LoginRequest` não tem tamanho máximo em `email` ou `senha`, e não há configuração de limite total de request em `application.properties`.

```java
public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String senha
)
```

**Cenário concreto:** um cliente anônimo envia JSON muito grande a `/auth/login` ou `/auth/cadastro`. Mesmo que a validação rejeite o valor, o servidor/proxy pode gastar memória e CPU lendo e parseando o corpo.

**Impacto:** degradação ou indisponibilidade com requisições concorrentes. O risco é **a confirmar** porque um proxy de produção pode já limitar o corpo.  
**Recomendação:** impor limite pequeno de corpo no proxy e no servidor, além de `@Size` em todos os campos do login. Rejeitar antes da desserialização quando o tamanho exceder o permitido.  
**Esforço:** P/M.

### SEC-04 — JWT é válido por 120 minutos e não há revogação no servidor

**Evidência**

```properties
security.jwt.expiracao-minutos=${JWT_EXPIRACAO_MINUTOS:120}
```

```java
.logout(AbstractHttpConfigurer::disable)
```

O token é HS256, assinado, exige segredo com ao menos 32 caracteres e valida emissor/expiração; porém não há `jti`, versão de sessão, denylist ou consulta ao usuário no banco durante a autenticação.

**Cenário concreto:** se um token for copiado do navegador, continuará permitindo acesso por até duas horas mesmo após o usuário clicar em “sair”, trocar a senha ou ter a conta/role alterada.

**Impacto:** janela de reutilização de token roubado.  
**Recomendação:** para o piloto, reduzir o TTL se aceitável e garantir que logout apague o token no frontend. Depois, adotar access token curto e mecanismo de revogação/rotação de refresh token ou versão de sessão no usuário.  
**Esforço:** P para reduzir TTL; M/G para sessão revogável completa.

### SEC-05 — Defesa contra prompt injection é apenas textual

**Evidência**

```java
"texto", neutralizar(solicitacao.textoRedacao())
```

`neutralizar` remove apenas tags `<redacao>`, e o texto é interpolado no mesmo conteúdo enviado como papel `user`. O template instrui o modelo a não obedecer ordens dentro da redação, mas isso não é uma fronteira de segurança.

**Cenário concreto:** o aluno insere instruções adversariais na redação para tentar obter nota máxima ou conteúdo escolhido nos campos de feedback. O parser impede formato arbitrário: exige exatamente cinco competências distintas e notas em `0/40/80/120/160/200`; portanto o risco restante é semântico, não execução de código nem acesso aos dados de outro aluno.

**Impacto:** integridade da correção e feedback manipulável. A extração do prompt não revela segredo de API, mas pode revelar as instruções de avaliação já presentes no repositório.  
**Recomendação:** colocar instruções estáveis em mensagem de sistema, enviar tema/texto como dados estruturados separados, usar schema estruturado da API, limitar comprimentos de todos os textos retornados e manter validações determinísticas. Criar testes adversariais de regressão; não tratar a saída como confiável.  
**Esforço:** M.

### SEC-06 — Mensagens de falha entram nos logs sem normalização

**Evidência**

```java
log.warn("Falha ao avaliar redação no Gemini (tentativa {}/{}): {}",
        tentativa, MAX_TENTATIVAS, ultimaFalha.getMessage());
log.warn("Correção da redação {} falhou: {}", redacao.id(), e.getMessage());
```

**Cenário concreto:** valores retornados pelo provedor — e, em algumas falhas de validação, valores gerados a partir do conteúdo induzido pelo aluno — podem conter quebras de linha e forjar visualmente entradas adicionais no log. Mensagens do cliente HTTP também podem expor detalhes internos do provedor/URL aos operadores.

**Impacto:** menor confiabilidade dos logs e exposição interna limitada. Não foi encontrado log de senha, token, texto completo da redação, resposta bruta ou chave.  
**Recomendação:** registrar código/tipo de falha e status em campos estruturados; remover CR/LF e limitar tamanho de mensagens externas. Nunca registrar headers ou corpo integral do Gemini.  
**Esforço:** P.

### SEC-07 — Arquivos locais sensíveis não estão ignorados

**Evidência**

O `.gitignore` cobre IDEs e `target/`, mas não `.env`, `.env.*`, `application-local.properties`, certificados ou keystores. `git check-ignore` não encontrou regra aplicável para `.env`, `.env.local` ou `application-local.properties`.

**Cenário concreto:** durante o deploy, alguém cria `.env` com `GEMINI_API_KEY`, `JWT_SECRET` e `DB_PASSWORD`; um `git add .` futuro pode versioná-lo.

**Impacto:** risco futuro de vazamento. Nenhum arquivo desse tipo está versionado hoje, e a busca no histórico alcançável encontrou apenas placeholders `${...}`.  
**Recomendação:** ignorar arquivos locais de ambiente/configuração e manter um exemplo sem segredos; usar secret manager no deploy.  
**Esforço:** P.

### SEC-08 — Resposta bruta do Gemini é armazenada sem política de retenção visível

**Evidência**

```java
json.writeValueAsString(avaliacao)
```

```sql
resposta_bruta_json JSONB
```

A resposta bruta repete conteúdo derivado da redação e pode conter trechos do texto do aluno. Ela não sai em `AvaliacaoResponse`, mas permanece no banco sem expiração observável.

**Cenário concreto:** em um incidente de banco ou acesso operacional indevido, há uma cópia adicional de dados educacionais/pessoais além da redação original.

**Impacto:** aumenta o volume e a superfície de dados pessoais armazenados.  
**Recomendação:** definir finalidade e prazo de retenção; se não for necessária para suporte/auditoria, não persistir ou remover após janela curta. Restringir privilégios e acesso operacional.  
**Esforço:** P/M.

### SEC-09 — Controles dependentes do deploy e dependências não verificados dinamicamente

**Evidência**

Não há no repositório configuração de proxy/HTTPS, `forward-headers-strategy`, limite total de request ou CSP/HSTS no proxy. Spring Security fornece cabeçalhos seguros padrão, mas HSTS só é emitido em contexto HTTPS. O banco usa fallback local (`localhost`, usuário `postgres`), enquanto a senha é obrigatória por ambiente.

Versões principais observadas: Java 17; Spring Boot parent 4.1.1; `java-jwt` 4.5.0; PostgreSQL/Flyway gerenciados pelo BOM do Spring Boot. Não foi executado scanner de CVEs porque isso exigiria dados/ferramentas externos, proibidos pelo escopo somente local; logo a ausência de vulnerabilidades de dependência **não está confirmada**.

**Cenário concreto:** configuração incorreta do proxy pode aceitar HTTP, calcular IP incorreto para rate limiting ou não emitir HSTS. Uma dependência pode ter CVE publicada não detectável apenas pelo `pom.xml`.

**Impacto:** a confirmar no ambiente real.  
**Recomendação:** terminar TLS no proxy, redirecionar HTTP, confiar `X-Forwarded-*` apenas do proxy, configurar forwarded headers conscientemente, adicionar limite de corpo e executar scanner de dependências no CI com base atualizada. Usar usuário PostgreSQL dedicado sem privilégios de superusuário; isso não é verificável pelo repositório.  
**Esforço:** M.

## 4. O que foi verificado e está OK

- **Segredos:** `DB_PASSWORD`, `GEMINI_API_KEY` e `JWT_SECRET` não têm fallback; não foi encontrado valor real no estado atual, collections Postman ou histórico alcançável. Os commits `f5771c9`, `f65e0ba` e `4889b21` continham apenas placeholders de ambiente. Os bundles rastreados apontam para commits já alcançáveis (`main` e `passo-14`).
- **JWT:** HS256, segredo mínimo de 32 caracteres, assinatura, issuer e expiração validados; token contém UUID, e-mail e role, mas não senha/hash/chave.
- **Senha:** BCrypt; cadastro exige 8–72 caracteres; DTOs mascaram senha em `toString`; login usa a mesma resposta para usuário inexistente e senha errada.
- **Autorização/IDOR:** nenhum endpoint aceita `usuarioId` do cliente. O UUID de usuário vem exclusivamente do principal autenticado. A consulta de redação por UUID compara `redacao.usuarioId` com o usuário autenticado e responde 404 em caso de outro dono.
- **Cota de IA:** reserva usa `INSERT ... ON CONFLICT DO UPDATE ... WHERE qtd_correcoes < :limite`, operação atômica no PostgreSQL; parâmetros são bindados. A vaga é liberada em falha.
- **SQL injection:** todas as queries nativas encontradas são constantes e parametrizadas; não foi encontrada concatenação de SQL.
- **Validação da IA:** JSON obrigatório, cinco competências distintas, números 1–5, notas somente nos seis valores oficiais, soma calculada no domínio, consistência de anulação e constraints equivalentes no banco.
- **Erros:** falha do Gemini retorna mensagem neutra ao cliente; stack trace e causa interna não são expostos por esse handler.
- **DTOs:** hash de senha, tokens de custo, modelo e resposta bruta da IA não são devolvidos ao aluno.
- **CORS:** origens explícitas/configuráveis, sem `*` e sem credenciais habilitadas. Cabeçalhos permitidos são restritos.
- **Persistência:** `ddl-auto=validate`, Flyway habilitado e constraints de domínio no banco. Não há Actuator no `pom.xml`.
- **Cabeçalhos:** Spring Security não desabilita os cabeçalhos de segurança padrão. CSRF desabilitado é coerente com autenticação Bearer stateless, desde que o token não migre para cookie automático sem reavaliar CSRF.

### Matriz endpoint → regra de acesso verificada

| Endpoint | Regra observada |
|---|---|
| `POST /auth/cadastro` | Público; cria somente `ALUNO`; problema de abuso em SEC-01/02 |
| `POST /auth/login` | Público; credenciais inválidas não enumeram usuário; problema de rate limit em SEC-02 |
| `POST /redacoes` | Autenticado; `usuarioId` vem do principal; cota reservada para esse usuário |
| `GET /redacoes` | Autenticado; repositório filtra por `usuarioId` do principal |
| `GET /redacoes/{id}` | Autenticado; serviço exige que a redação pertença ao principal; outro dono recebe 404 |
| `GET /temas` | Autenticado; temas globais + previsões vinculadas ao próprio principal |
| `GET /temas/possiveis` | Autenticado; marca seleção conforme o próprio principal |
| `GET /usuarios/me` | Autenticado; devolve somente o principal do token |
| `GET /usuarios/me/correcoes-hoje` | Autenticado; consulta pelo UUID do principal |
| `GET /usuarios/me/evolucao` | Autenticado; redações e avaliações derivadas das redações do principal |
| `PUT /usuarios/me/temas/{temaId}` | Autenticado; vínculo criado com UUID do principal; só tema `PREVISAO` ativo |
| `DELETE /usuarios/me/temas/{temaId}` | Autenticado; remoção restringida ao UUID do principal |

### Itens não aplicáveis neste repositório

- Frontend, armazenamento do JWT no navegador, XSS, logout/cache do TanStack Query, `VITE_*`, source maps, build Vite e `npm audit`: o frontend `redacao-sm-frontend` não está presente.
- Dockerfile/docker-compose: não existem no conjunto de arquivos rastreados.
- Actuator: dependência/configuração não existe.

## 5. Comandos executados

Os comandos abaixo foram usados somente para leitura. As buscas com conteúdo potencialmente sensível tiveram saída mascarada antes da exibição.

```powershell
Get-Content -Raw docs/AUDITORIA_SEGURANCA_PROMPT.md
git ls-files
git status --short
git log --oneline -20
git log --all -p --full-history
git log --all --fixed-strings -S <padrão> --format='%H %ad %s' --date=short --no-patch
git show <commit>:src/main/resources/application.properties
git show-ref
git bundle list-heads .claude-sync.bundle
git bundle list-heads passo-14.bundle
git check-ignore -v .env .env.local application-local.properties
git grep -n -I -E <padrões-de-segredo> HEAD
git grep -n -i 'actuator\|management.endpoints\|server.forward-headers\|server.tomcat\|max-http-request' HEAD
Get-ChildItem -Recurse -File
Get-Content <arquivos relevantes com numeração de linhas>
Select-String -Pattern <endpoints|segurança|SQL|logs|segredos>
```

Não rodei Maven/testes/scanners para respeitar a regra de que o único arquivo criado ou modificado pela auditoria fosse este relatório; builds podem escrever em `target/`. Também não consultei bancos de CVE ou serviços externos.
