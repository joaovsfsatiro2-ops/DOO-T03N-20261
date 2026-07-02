# Atividade Extra - Aula 13
Nome: João Vitor

------------------------------------------------------------------------

## Conceito 1: cgroups (Control Groups)

**Timestamp do vídeo:** 2:16

### O que é?

cgroups (Control Groups) é um recurso do kernel Linux que permite
limitar, controlar e monitorar o uso de recursos do sistema por um grupo
de processos.

### Para que serve?

Serve para controlar quanto de CPU, memória, disco e outros recursos um
programa pode utilizar. Isso impede que um único processo consuma todos
os recursos da máquina.

### Como é normalmente utilizado?

É muito utilizado em containers, como Docker e Kubernetes, para garantir
que cada container utilize apenas a quantidade de recursos definida pelo
administrador.

### Exemplo

``` bash
systemd-run --scope -p MemoryMax=256M programa
```

------------------------------------------------------------------------

## Conceito 2: SELinux (Security-Enhanced Linux)

**Timestamp do vídeo:** 2:42

### O que é?

SELinux é um sistema de segurança do Linux baseado em controle
obrigatório de acesso (MAC - Mandatory Access Control). Ele adiciona uma
camada extra de proteção além das permissões tradicionais de arquivos.

### Para que serve?

Serve para impedir que programas acessem arquivos ou recursos que não
deveriam acessar, reduzindo os impactos de falhas de segurança ou
invasões.

### Como é normalmente utilizado?

É muito utilizado em servidores Linux, principalmente em distribuições
como Red Hat, CentOS e Fedora, para aumentar a segurança do sistema.

### Exemplo

Verificar o status do SELinux:

``` bash
getenforce
```

Saída possível:

``` text
Enforcing
```
