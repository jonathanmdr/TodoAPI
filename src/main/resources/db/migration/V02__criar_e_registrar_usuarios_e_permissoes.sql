CREATE TABLE usuario (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL,
    senha VARCHAR(150) NOT NULL
);

CREATE TABLE permissao (
    id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(50) NOT NULL
);

CREATE TABLE usuario_permissao (
    id_usuario BIGINT(20) NOT NULL,
    id_permissao BIGINT(20) NOT NULL,
    PRIMARY KEY (id_usuario, id_permissao),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id),
    FOREIGN KEY (id_permissao) REFERENCES permissao(id)
);

INSERT INTO usuario (id, nome, email, senha) VALUES(1, 'Administrador', 'admin@todoapi.com', '$2a$10$dtxzKhZU9vfPEdh29/4rMeqlNyXAtnxQii2mSqf4Jx93q/i5LQUGq');

INSERT INTO permissao (id, descricao) VALUES(1, 'ROLE_CADASTRAR_TAREFA'),
                                            (2, 'ROLE_PESQUISAR_TAREFA'),
                                            (3, 'ROLE_REMOVER_TAREFA');

-- admin (Todas Permissões)
INSERT INTO usuario_permissao (id_usuario, id_permissao) VALUES(1, 1),
                                                               (1, 2),
                                                               (1, 3);