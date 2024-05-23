<h1 align="center">PharmacyManage API</h1>

<p align="center">
  <a href="#-tecnologias">Tecnologias</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-projeto">Projeto</a>&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;
  <a href="#-features">Features</a>&nbsp;&nbsp;&nbsp;|
  <a href="#-equipe">Equipe</a>&nbsp;&nbsp;
</p>

<br>

## 🚀 Tecnologias

Esse projeto foi desenvolvido com as seguintes tecnologias:

| Tecnologia                                                                                                           | Funcionalidade                                                                                                                              |
|----------------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------|
| ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)            | linguagem utilizada de forma básica e avançada para estruturas de decisão e repetição, orientação a objetos e mapeamento objeto-relacional. |
| ![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white) | como fonte de dados, fazendo uso da linguagem SQL para persistência dos dados.                                                              |
| ![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)         | framework para construção de uma API REST completa e funcional.                                                                             | 
| ![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white)                  | versionamento de código em repositório local.                                                                                               |
| ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)         | versionamento de código em repositório remoto.                                                                        

## 💻 Projeto

O presente trabalho é uma atividade desenvolvida durante o módulo final do curso DevInHouse, Turma Clamed V2, executado com uso do framework SPRING BOOT. O projeto consiste em uma API REST voltada para o gerenciamento interno de farmácias, seus estoques e medicamentos. Este repositório contém o código fonte da API REST projetada para demonstrar os conceitos e funcionalidades solicitados no projeto. Cada branch contém o código fonte do respectivo resquisito funcional.

#### Pré-requisitos:
O projeto é desenvolvido com Maven, Java 20 e Spring Boot 4.1.0.
Para rodar a aplicação é necessário a instalação do Java 17 (ou posterior) e também do PostgreSQL na máquina.

### Métodos

Requisições para a API devem seguir os padrões:

| Método | Descrição |
|---|---|
| `GET` | Retorna informações de um ou mais registros. |
| `POST` | Utilizado para criar um novo registro. |
| `PUT` | Atualiza dados de um registro ou altera sua situação. |
| `DELETE` | Remove um registro do sistema. |

### Respostas

| Código | Descrição |
|---|---|
| `200` | Requisição executada com sucesso (200 OK).|
| `201` | Registro criado com sucesso (201 CREATED).|
| `400` | Solicitação com erros de validação ou os campos informados inexistentes no sistema (400 BAD REQUEST).|
| `404` | Registro não encontrado (404 NOT FOUND).|

## 📄 Features

A seguir estão listados os requisitos funcionais do projeto, bem como um  breve trecho do código desenvolvido para executar cada requisito.
Para ver o código, seus métodos e funções na totalidade, acesse os arquivos dentro do repositório.


- [x] RF01 - Carga inicial de dados
  Executa um script SQL ao iniciar a aplicação, inserindo informações na base de dados através de um processo automatizado que valida se os dados já existem no banco, evitando que ocorram inserções de forma repetida.

- [x] RF02 - Consulta de Farmácias
```http
  GET /farmacias
```
~~~~Java
    public Page<FarmaciaResponse> consultarFarmacias(Pageable pageable) {
        Page<Farmacia> farmacias = farmaciaRepository.findAll(pageable);
        if (farmacias.isEmpty()) {
            throw new NenhumRegistroEncontradoException("Nenhum registro encontrado na lista de farmácias.");
        }
        return farmacias.map(FarmaciaResponse::new);
    }
~~~~

- [x] RF03 - Consulta de Farmácia pelo CNPJ
```http
  GET /farmacias/{cnpj}
```
~~~~Java
      public Farmacia consultarFarmacia(Long cnpj) {
        return farmaciaRepository.findById(cnpj)
                .orElseThrow(() -> new RegistroNaoEncontradoException("Farmacia", cnpj));
    }
~~~~

- [x] RF04 - Consulta de CEP para endereço
```http
  GET /enderecos/{cep}/busca
```
~~~~Java      
        public EnderecoResponse consultarEnderecoViaCep(String cep) {
        try {
            EnderecoCepResponse response = cepClient.consultarCep(cep);
            return EnderecoMapper.mapToEnderecoResponse(response);
        } catch (FeignException.BadRequest e) {
            throw new CepNaoEncontradoException(cep);
        } catch (FeignException e) {
            throw new RuntimeException();
        }
~~~~

- [x] RF05 - Cadastro de Farmácia
```http
  POST /farmacias
```
~~~~Java
       public FarmaciaResponse registrarFarmacia(@NotNull FarmaciaRequest farmaciaRequest) {
          Farmacia farmacia = modelMapper.map(farmaciaRequest, Farmacia.class);
          boolean registroJaCadastrado = farmaciaRepository.existsById(farmacia.getCnpj());
          if (registroJaCadastrado)
              throw new RegistroJaCadastradoException("Farmacia", farmacia.getCnpj());
          farmacia = farmaciaRepository.save(farmacia);
          return new FarmaciaResponse(farmacia);
      }
~~~~

- [x] RF06 - Cadastro de telefones
```http
  POST /farmacias/{cnpj}/telefones
```
~~~~~Java
   public TelefoneResponse registrarTelefone(Long cnpj, TelefoneRequest telefoneRequest) {
        Farmacia farmacia = farmaciaService.consultarFarmacia(cnpj);
        Telefone telefone = new Telefone();
        BeanUtils.copyProperties(telefoneRequest, telefone);
        farmacia.getTelefone().add(telefone);
        telefone = telefoneRepository.save(telefone);
        return new TelefoneResponse(telefone);
    }
~~~~~

- [x] RF07 - Deleção de um telefone
```http
  DELETE /farmacias/{cnpj}/telefones/{id}
```
~~~~~Java
    public void removeTelefonePorId(Long id, Long cnpj) {
        var optionalTelefone = telefoneRepository.findById(id);
        if (optionalTelefone.isEmpty()) {
            throw new RegistroNaoEncontradoException();
        }
        removeTelefoneDeFarmacia(farmaciaService.consultarFarmacia(cnpj), optionalTelefone.get());
        telefoneRepository.delete(optionalTelefone.get());
    }

    private void removeTelefoneDeFarmacia(Farmacia farmacia, Telefone telefone) {
        farmacia.getTelefone().remove(telefone);
    }
~~~~~

- [x] RF08 - Consulta de Medicamentos
```http
  GET /medicamentos
```
~~~~Java
 
    public Page<MedicamentoResponse> consultarMedicamentos(Pageable pageable) {
        Page<Medicamento> medicamentos = medicamentoRepository.findAll(pageable);
        if (medicamentos.isEmpty()) {
            throw new NenhumRegistroEncontradoException("Nenhum registro encontrado na lista de medicamentos.");
        }
        return medicamentos.map(this::convertToResponse);
    }
~~~~~

- [x] RF09 - Cadastro de medicamentos
```http
  POST /medicamentos
```
~~~~Java
 public MedicamentoResponse registrarMedicamento(MedicamentoRequest request) {

        Medicamento medicamento = modelMapper.map(request, Medicamento.class);
        boolean registroJaCadastrado = medicamentoRepository.existsById(medicamento.getNroRegistro());

        if (registroJaCadastrado) {
            throw new RegistroJaCadastradoException("Medicamento", medicamento.getNroRegistro());
        }

        medicamento = medicamentoRepository.save(medicamento);
        return modelMapper.map(medicamento, MedicamentoResponse.class);
    }
~~~~

- [x] RF10 - Consulta de Estoque de Farmácia
```http
  GET /farmacias/{cnpj}/estoque
```
~~~~Java
    public List<Estoque> consultarEstoque(Long cnpj) {
        return estoqueRepository.findAllByCnpj(cnpj);
    }

    public Page<EstoqueResponse> consultarEstoques(Pageable pageable, Long cnpj) {
        Page<Estoque> estoques = estoqueRepository.findAllByCnpj(cnpj, pageable);
        if (estoques.isEmpty()) {
            throw new NenhumRegistroEncontradoException("Nenhum registro encontrado na lista de estoque.");
        }
        return estoques.map(this::convertToResponse);
    }  
~~~~

- [x] RF11 - Aquisição de Medicamentos para o estoque da farmácia
```http
  POST /farmacias/{cnpj}/estoque
```
~~~~Java
    public Estoque registrarEstoque(EstoqueRequest estoqueRequest) {
        Farmacia farmacia = farmaciaService.consultarFarmacia(estoqueRequest.getCnpj());
        Medicamento medicamento = medicamentoService.consultarMedicamento(estoqueRequest.getNroRegistro());
        if (estoqueRequest.getQuantidade() <= 0) {
            throw new QuantidadeInvalidaException("Quantidade", estoqueRequest.getQuantidade());
        }
        IdEstoque id = new IdEstoque(farmacia.getCnpj(), medicamento.getNroRegistro());
        if (!(estoqueRepository.existsById(id))) {
            Estoque estoque = new Estoque(farmacia.getCnpj(), medicamento.getNroRegistro().longValue(), estoqueRequest.getQuantidade(), LocalDateTime.now());
            estoque = estoqueRepository.save(estoque);
            return estoque;
        }
        Estoque estoque = estoqueRepository.getReferenceById(id);
        estoque.setQuantidade(estoque.getQuantidade() + estoqueRequest.getQuantidade());
        estoque.setDataAtualizacao(LocalDateTime.now());
        estoque = estoqueRepository.save(estoque);
        return estoque;
    }
~~~~

- [x] RF12 - Atualização do estoque ao vender um medicamento
```http
  PUT /farmacias/{cnpj}/estoque
```
~~~~Java
    public Estoque venderEstoque(EstoqueRequest estoqueRequest) {
        Farmacia farmacia = farmaciaService.consultarFarmacia(estoqueRequest.getCnpj());
        Medicamento medicamento = medicamentoService.consultarMedicamento(estoqueRequest.getNroRegistro());
        if (estoqueRequest.getQuantidade() <= 0) {
            throw new QuantidadeInvalidaException("Quantidade", estoqueRequest.getQuantidade());
        }
        IdEstoque id = new IdEstoque(farmacia.getCnpj(), medicamento.getNroRegistro());
        if (!(estoqueRepository.existsById(id))) {
            throw new RegistroNaoEncontradoException("Estoque", estoqueRequest.getNroRegistro().toString());
        }
        Estoque estoque = estoqueRepository.getReferenceById(id);
        if (estoqueRequest.getQuantidade() > estoque.getQuantidade()) {
            throw new QuantidadeInvalidaException("Estoque", estoqueRequest.getQuantidade());
        }
        estoque.setQuantidade(estoque.getQuantidade() - estoqueRequest.getQuantidade());
        estoque.setDataAtualizacao(LocalDateTime.now());
        if (estoque.getQuantidade() == 0) {
            estoqueRepository.delete(estoque);
            return estoque;
        }
        estoque = estoqueRepository.save(estoque);
        return estoque;
    }
~~~~
- [x] RF13 - Troca de medicamentos entre estoques de farmácias:
```http
  PUT /farmacias/{cnpj}/estoque/troca
```
~~~~Java
public List<Estoque> transferenciaEstoque(EstoqueTransferenciaRequest request) {
        farmaciaService.consultarFarmacia(request.getCnpjOrigem());
        farmaciaService.consultarFarmacia(request.getCnpjDestino());
        medicamentoService.consultarMedicamento(request.getNroRegistro());
        var estoqueOrigem = venderEstoque(new EstoqueRequest(request.getCnpjOrigem(), request.getNroRegistro(), request.getQuantidade()));
        var estoqueDestino = registrarEstoque(new EstoqueRequest(request.getCnpjDestino(), request.getNroRegistro(), request.getQuantidade()));
        return Arrays.asList(estoqueOrigem, estoqueDestino);
    }
~~~~ 

## 💾 Iniciando o Projeto
Primeiro clone este repositório remoto em sua máquina local:

[https://github.com/DEVinHouse-Clamed-V2/M3P-BackEnd-Squad4](https://github.com/DEVinHouse-Clamed-V2/M3P-BackEnd-Squad4)

Lembre-se de adicionar as dependências do projeto maven.


Realize a configuração do seu banco de dados local em ```M3P-BackEnd-Squad4/src/main/resources/application.yml```:

```bash
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```
Inicie a aplicação em sua IDE de preferência.

## 📝 Equipe
<p>Esse projeto foi desenvolvido pelo grupo DevSync.</p>

| Desnvolvedores | Github |
| ---            | ---    |
| Camila Artigas De Pra | camilartigas |
| Denison Perin Kolling | denisonkolling |
| Fernanda Ribeiro Coutinho | feecoutinho |
| Jader Luiz Maciel Do Nascimento | jaderm1 |
| Lucas Vinicius Damasceno Alves | Lucasvdalves |
| Maria Nilda Vicente Caetano | mavicaetano |