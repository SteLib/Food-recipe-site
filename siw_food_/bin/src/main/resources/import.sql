insert into ricetta (id, nome, url_image, descrizione) values(nextval('ricetta_seq'), 'Carbonara', '/images/carbonara.jpg', 'La pasta alla carbonara è un piatto tipico della tradizione italiana e più in particolare del Lazio, assurta nel tempo come uno dei simboli culinari della città di Roma.');
insert into ricetta (id, nome, url_image, descrizione) values(nextval('ricetta_seq'), 'Amatriciana', '/images/amatriciana.jpg','La amatriciana (matriciana in romanesco) è un condimento per la pasta tipico della tradizione gastronomica di Amatrice, cittadina in provincia di Rieti, nella regione Lazio.');
insert into ricetta (id, nome, url_image, descrizione) values(nextval('ricetta_seq'), 'Cacio e pepe', '/images/cacioepepe.jpg', 'La cacio e pepe è un piatto caratteristico del Lazio. Come suggerisce il nome, gli ingredienti sono molto semplici e includono solo pepe nero, formaggio pecorino romano e pasta.');
insert into ricetta (id, nome, url_image, descrizione) values(nextval('ricetta_seq'), 'Pasta e fagioli', '/images/pastaefagioli.jpg', 'Pasta e fagioli è un piatto tipico italiano di cui esistono diverse varianti regionali. La regione di origine è incerta, essendo considerato un piatto tipico della cucina di varie regioni, ad esempio: Calabria, Lazio, Campania, Toscana.');
insert into ricetta (id, nome, url_image, descrizione) values(nextval('ricetta_seq'), 'Pagliata', '/images/pagliata.jpg', 'La pagliata o, in romanesco, pajata, è l intestino tenue del vitellino da latte o del bue che viene utilizzato soprattutto per la preparazione di un tipico piatto di pasta della cucina romana, i rigatoni con la pajata, per il quale si usa il secondo tratto dellintestino tenue, denominato "digiuno".');


insert into cuoco (id, nome, cognome, url_image, data_di_nascita) values(nextval('cuoco_seq'), 'Bruno', 'Barbieri', '/images/brunobarbieri.jpg','1962-01-12');
insert into cuoco (id, nome, cognome, url_image, data_di_nascita) values(nextval('cuoco_seq'), 'Antonio', 'Cannavacciuolo', '/images/antoniocannavacciuolo.jpg','1975-04-16');
insert into cuoco (id, nome, cognome, url_image, data_di_nascita) values(nextval('cuoco_seq'), 'Carlo', 'Cracco', '/images/carlocracco.jpg','1965-10-08');
insert into cuoco (id, nome, cognome, url_image, data_di_nascita) values(nextval('cuoco_seq'), 'Alessandro', 'Borghese', '/images/alessandroborghese.png','1976-11-19');
insert into cuoco (id, nome, cognome, url_image, data_di_nascita) values(nextval('cuoco_seq'), 'Gordon', 'Ramsay', '/images/gordonramsay.jpg','1966-11-08');

insert into ingrediente (id, nome, quantita) values(nextval('ingrediente_seq'), 'Spaghetti', '320 g');
insert into ingrediente (id, nome, quantita) values(nextval('ingrediente_seq'), 'Guanciale', '150 g');
insert into ingrediente (id, nome, quantita) values(nextval('ingrediente_seq'), 'Tuorli', '6');
insert into ingrediente (id, nome, quantita) values(nextval('ingrediente_seq'), 'Pecorino romano', '50 g');
insert into ingrediente (id, nome, quantita) values(nextval('ingrediente_seq'), 'Pepe nero', 'q.b.');