# Projeto Locadora de Veículos
 Trabalho para composição de nota nas disciplinas Testes de Software e Banco de Dados I do semestre 2025.1, realizado por alunos do terceiro (3º) período do curso Superior em Sistemas Para Internet, da Universidade Estadual de Ciências da Saúde de Alagoas (UNCISAL).  

    
## Informações básicas sobre o projeto
- O software deverá ser criado utilizando a linguagem JAVA;
- Basicamente, o software deve conter 04 funcionalidades básicas: **criar, ler, atualizar e deletar**;
- O Banco de Dados a ser utilizado deverá ser o **MySQL**;
- Para criar o contâiner docker com o banco de dados, basta rodar o seguinte comando no diretório raiz do projeto: **docker compose up** (É importante possuir o docker instalado e de que as portas 3306:3306 estão liberadas para o container rodar!).

### Pontos a serem avaliados
- Software;
- Apresentação (Todos devem apresentar);
- Frequência (nas aulas presenciais e remotas, destinadas para o desenvolvimento do projeto);
- Participação (no desenvolvimento do projeto).

### Datas importantes
- NUP02 -> Projeto Prático (JUnit) (03/06);
- 1° etapa do projeto em BD -> Montar o diagrama de entidades e relacionamentos (08/05);
- 1° Etapa do projeto Testes (27/05);
- 2° Etapa de BD (22/05);
- 2° Etapa Testes (03/06);
- 3° Etapa de BD (05/06) (SUBMISÃO DE CONSULTAS SQL);
- 3° Etapa Testes (10/06) (AQUI SERÁ A APRESENTAÇÃO).


### Requisitos importantes para o programa funcionar devidamente durante a apresentação:
- O banco de dados, ao ser criado como novo, usando o docker compose, está sem dados. **É necessário colocar as categorias do veículo na tabela "categoria_veiculos" e cadastrar ao menos 1 (um) funcionário na tabela "funcionario"**.

- Rodar **"docker compose down"** e **"docker compose down -v"** para excluir o contâiner e volume do contâiner. Após isso, rodar o seguinte comando para criar e subir o contâiner com o banco de dados **"docker compose up -d --build"** (é necessário estar no mesmo diretório do arquivo docker-compose.yml).