CREATE TABLE tarefa (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(60) NOT NULL,
    descricao VARCHAR(255),
    situacao INT NOT NULL,
    data_criacao DATE,
    data_edicao DATE,
    data_conclusao DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;