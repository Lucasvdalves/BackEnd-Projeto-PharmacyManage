INSERT INTO enderecos(id, cep, logradouro, numero, bairro, cidade, estado, complemento)
VALUES  (1, 88888999, 'Rua Porto Real', 67, 'Westeros', 'Berlim', 'SC', 'Não possui'),
        (2, 8877799, 'Rua Madrid', 76, 'Winterfell', 'Estocolmo', 'SC', 'Não possui');
SELECT setval('enderecos_id_seq', max(id)) FROM enderecos;

INSERT INTO farmacias(cnpj, email, nome_fantasia, razao_social, endereco_id)
VALUES  (90561736000121, 'devmed@farmacia.com', 'Farmácia DevMed', 'DevMed Ltda', 1),
        (43178995000198, 'medhouse@farmacia.com', 'Farmácia MedHouse', 'MedHouse Ltda', 2);


INSERT INTO telefones(id, codigo_pais, codigoddd, numero_telefone, farmacia_cnpj)
VALUES  (1, 55, 55, 955555555, 90561736000121),
        (2, 55, 55, 966666666, 43178995000198);
SELECT setval('telefones_id_seq', max(id)) FROM telefones;


INSERT INTO farmacias_telefone(farmacia_cnpj, telefone_id)
SELECT farmacias.cnpj, telefones.id
FROM farmacias, telefones
WHERE farmacias.cnpj = telefones.farmacia_cnpj;

INSERT INTO medicamentos(nro_registro, descricao, dosagem, laboratorio, nome, preco, tipo)
VALUES  (1010, 'Lorem ipsum dolor', '2x ao dia', 'Matrix', 'Programapan', 111.00, 'COMUM'),
        (7473, 'Lorem ipsum dolor', '1x ao dia', 'Colombia Farm', 'Cafex', 51.50, 'CONTROLADO');

INSERT INTO estoques(cnpj, nro_registro, quantidade, data_atualizacao)
VALUES  (90561736000121, 1010, 200, CURRENT_TIMESTAMP),
        (43178995000198, 7473, 150, CURRENT_TIMESTAMP);