# API de Controle Parental

A **API de Controle Parental** foi desenvolvida para permitir que pais e responsáveis monitorem e gerenciem a atividade online dos seus filhos. A API oferece funcionalidades para registrar e autenticar usuários, gerenciar contas de filhos, bloquear URLs e aplicativos, além de monitorar o histórico de sites visitados e o tempo de uso dos dispositivos. Ela também permite que os responsáveis gerem **relatórios semanais e mensais**, proporcionando uma visão consolidada da atividade online dos filhos ao longo do tempo, garantindo um ambiente digital mais seguro.

## Funcionalidades principais

### Gestão de Usuários:
- **Criação de Usuários**: Os pais e filhos podem criar contas, e um e-mail de verificação será enviado ao usuário para confirmar o registro.
- **Autenticação de Usuários**: Após a verificação de e-mail, o usuário pode se autenticar para acessar a plataforma.
- **Verificação de E-mail**: Envio de um e-mail de verificação após o cadastro. O link no e-mail contém um token único para ativar a conta.
- **Associar Contas de Filhos a Pais**: Durante o processo de cadastro, os pais podem associar seus filhos às suas contas, adicionando o **e-mail do filho**. Um link será enviado para o e-mail do filho, e ele deverá clicar no link para confirmar a associação e entrar na família.

### Controle de Sites e Aplicativos:
- **Bloquear URLs**: Bloquear URLs específicas para impedir o acesso a conteúdos inadequados.
- **Monitoramento e Bloqueio de Aplicativos em Tempo Real**: Bloqueio e monitoramento de aplicativos em tempo real.
- **Listagem de Aplicativos Abertos e Tempo de Uso**: Exibição dos aplicativos abertos e o tempo de uso de cada um.

### Histórico de Navegação:
- **Coleta de Histórico de Navegação**: Coleta e exibição do histórico de sites visitados pelos filhos.
- **Relatórios sobre Tempo Gasto**: Relatórios sobre o tempo gasto em cada site/aplicativo, com a possibilidade de visualizar dados semanais e mensais.

### Relatórios Semanais e Mensais:
- **Relatórios Semanais**: Geração de relatórios semanais detalhados sobre os sites visitados, o tempo gasto em cada site e aplicativos utilizados durante a semana.
- **Relatórios Mensais**: Geração de relatórios mensais que fornecem uma visão consolidada das atividades online durante o mês, permitindo aos pais verem o comportamento digital dos filhos ao longo de um período mais longo.

### Integração com Aplicativo Monitorador:
- **Integração com Aplicativo Monitorador**: O sistema se comunica com um aplicativo monitorador desenvolvido em Python para coletar dados de navegação e bloquear URLs, conforme as configurações feitas pelos pais. A API externa permite a interação com esse aplicativo, garantindo que as ações de monitoramento e bloqueio sejam realizadas de forma eficaz.

### Autenticação e Segurança:
- **Autenticação via JWT**: Implementação de autenticação via token JWT para garantir segurança no acesso à API.
- **OAuth**: Implementação de OAuth para permitir um login mais flexível e seguro.

### Documentação e Testes:
- **Swagger**: Documentação gerada automaticamente usando Swagger.

## Como Funciona

1. **Criação de Conta de Pai e Filho**: Os pais podem criar suas contas e, ao criar uma conta para o filho, um token único é gerado para associar o filho ao pai. Após o cadastro, um e-mail de verificação é enviado para a confirmação da conta.

2. **Verificação de E-mail**: Ao clicar no link de verificação enviado para o e-mail, a conta é ativada e o usuário pode começar a usar a plataforma.

3. **Gerenciamento de Contas**: Após o login, pais podem gerenciar os filhos associados à sua conta, incluindo o bloqueio de URLs, monitoramento de aplicativos e o acesso ao histórico de navegação.

4. **Geração de Relatórios**: Os pais podem acessar relatórios semanais e mensais que detalham a atividade online dos filhos, incluindo sites visitados, tempo de uso e aplicativos utilizados.


## Tecnologias utilizadas

- **Spring Boot**: Framework para desenvolvimento da API.
- **Spring Data JPA**: Para persistência de dados no banco de dados.
- **Spring Security**: Para gerenciamento de autenticação de usuários.
- **Swagger**: Para a documentação da API.
- **Spring Cloud OpenFeign**: Para integração com APIs externas.
- **JWT (JSON Web Token)**: Para autenticação segura de usuários.
- **JavaMailSender**: Para envio de e-mails de verificação.
- **OAuth**: Para autenticação de usuários de forma flexível.

## Objetivo do Projeto

O objetivo desta API é proporcionar um sistema de controle parental eficaz, onde os pais podem gerenciar e monitorar a atividade online dos seus filhos, garantindo que tenham uma experiência digital segura e adequada à sua idade. A API foi desenvolvida com foco em simplicidade, segurança e desempenho.

## Como Começar

Clone o repositório:
```bash
git clone https://github.com/seu-usuario/api-controle-parental.git
