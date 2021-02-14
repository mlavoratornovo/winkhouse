UPDATE PUBLIC.ABBINAMENTI A SET CODIMMOBILE=NULL WHERE (SELECT COUNT(CODIMMOBILE) FROM IMMOBILI WHERE CODIMMOBILE = A.CODIMMOBILE) = 0;
UPDATE PUBLIC.ABBINAMENTI A SET CODANAGRAFICA=NULL WHERE (SELECT COUNT(CODANAGRAFICA) FROM PUBLIC.ANAGRAFICHE WHERE CODANAGRAFICA = A.CODANAGRAFICA) = 0;
UPDATE PUBLIC.ABBINAMENTI A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.ABBINAMENTI ADD FOREIGN KEY (CODIMMOBILE) REFERENCES PUBLIC.IMMOBILI(CODIMMOBILE);
ALTER TABLE PUBLIC.ABBINAMENTI ADD FOREIGN KEY (CODANAGRAFICA) REFERENCES PUBLIC.ANAGRAFICHE(CODANAGRAFICA);
ALTER TABLE PUBLIC.ABBINAMENTI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.AFFITTI A SET CODIMMOBILE=NULL WHERE (SELECT COUNT(CODIMMOBILE) FROM IMMOBILI WHERE CODIMMOBILE = A.CODIMMOBILE) = 0;
UPDATE PUBLIC.AFFITTI A SET CODAGENTEINS=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
UPDATE PUBLIC.AFFITTI A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.AFFITTI ADD FOREIGN KEY (CODIMMOBILE) REFERENCES PUBLIC.IMMOBILI(CODIMMOBILE);
ALTER TABLE PUBLIC.AFFITTI ADD FOREIGN KEY (CODAGENTEINS) REFERENCES PUBLIC.AGENTI(CODAGENTE);
ALTER TABLE PUBLIC.AFFITTI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.AFFITTIALLEGATI A SET CODAFFITTO=NULL WHERE (SELECT COUNT(CODAFFITTI) FROM AFFITTI WHERE CODAFFITTI = A.CODAFFITTO) = 0;
UPDATE PUBLIC.AFFITTIALLEGATI A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.AFFITTIALLEGATI ADD FOREIGN KEY (CODAFFITTO) REFERENCES PUBLIC.AFFITTI(CODAFFITTI);
ALTER TABLE PUBLIC.AFFITTIALLEGATI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.AFFITTIANAGRAFICHE A SET CODAFFITTO=NULL WHERE (SELECT COUNT(CODAFFITTI) FROM AFFITTI WHERE CODAFFITTI = A.CODAFFITTO) = 0;
UPDATE PUBLIC.AFFITTIANAGRAFICHE A SET CODANAGRAFICA=NULL WHERE (SELECT COUNT(CODANAGRAFICA) FROM PUBLIC.ANAGRAFICHE WHERE CODANAGRAFICA = A.CODANAGRAFICA) = 0;
UPDATE PUBLIC.AFFITTIANAGRAFICHE A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.AFFITTIANAGRAFICHE ADD FOREIGN KEY (CODAFFITTO) REFERENCES PUBLIC.AFFITTI(CODAFFITTI);
ALTER TABLE PUBLIC.AFFITTIANAGRAFICHE ADD FOREIGN KEY (CODANAGRAFICA) REFERENCES PUBLIC.ANAGRAFICHE(CODANAGRAFICA);
ALTER TABLE PUBLIC.AFFITTIANAGRAFICHE ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.AFFITTIRATE A SET CODAFFITTO=NULL WHERE (SELECT COUNT(CODAFFITTI) FROM AFFITTI WHERE CODAFFITTI = A.CODAFFITTO) = 0;
UPDATE PUBLIC.AFFITTIRATE A SET CODPARENT=NULL WHERE (SELECT COUNT(CODAFFITTIRATE) FROM AFFITTIRATE WHERE CODAFFITTIRATE = A.CODPARENT) = 0;
UPDATE PUBLIC.AFFITTIRATE A SET CODANAGRAFICA=NULL WHERE (SELECT COUNT(CODANAGRAFICA) FROM PUBLIC.ANAGRAFICHE WHERE CODANAGRAFICA = A.CODANAGRAFICA) = 0;
UPDATE PUBLIC.AFFITTIRATE A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.AFFITTIRATE ADD FOREIGN KEY (CODAFFITTO) REFERENCES PUBLIC.AFFITTI(CODAFFITTI);
ALTER TABLE PUBLIC.AFFITTIRATE ADD FOREIGN KEY (CODPARENT) REFERENCES PUBLIC.AFFITTIRATE(CODAFFITTIRATE);
ALTER TABLE PUBLIC.AFFITTIRATE ADD FOREIGN KEY (CODANAGRAFICA) REFERENCES PUBLIC.ANAGRAFICHE(CODANAGRAFICA);
ALTER TABLE PUBLIC.AFFITTIRATE ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.AFFITTISPESE A SET CODAFFITTO=NULL WHERE (SELECT COUNT(CODAFFITTI) FROM AFFITTI WHERE CODAFFITTI = A.CODAFFITTO) = 0;
UPDATE PUBLIC.AFFITTISPESE A SET CODPARENT=NULL WHERE (SELECT COUNT(CODAFFITTISPESE) FROM PUBLIC.AFFITTISPESE WHERE CODAFFITTISPESE = A.CODPARENT) = 0;
UPDATE PUBLIC.AFFITTISPESE A SET CODANAGRAFICA=NULL WHERE (SELECT COUNT(CODANAGRAFICA) FROM PUBLIC.ANAGRAFICHE WHERE CODANAGRAFICA = A.CODANAGRAFICA) = 0;
UPDATE PUBLIC.AFFITTISPESE A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.AFFITTISPESE ADD FOREIGN KEY (CODAFFITTO) REFERENCES PUBLIC.AFFITTI(CODAFFITTI);
ALTER TABLE PUBLIC.AFFITTISPESE ADD FOREIGN KEY (CODPARENT) REFERENCES PUBLIC.AFFITTISPESE(CODAFFITTISPESE);
ALTER TABLE PUBLIC.AFFITTISPESE ADD FOREIGN KEY (CODANAGRAFICA) REFERENCES PUBLIC.ANAGRAFICHE(CODANAGRAFICA);
ALTER TABLE PUBLIC.AFFITTISPESE ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.AGENTI A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.AGENTI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.AGENTIAPPUNTAMENTI A SET CODAGENTE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
UPDATE PUBLIC.AGENTIAPPUNTAMENTI A SET CODAPPUNTAMENTO=NULL WHERE (SELECT COUNT(CODAPPUNTAMENTO) FROM APPUNTAMENTI WHERE CODAPPUNTAMENTO = A.CODAPPUNTAMENTO) = 0; 
UPDATE PUBLIC.AGENTIAPPUNTAMENTI A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.AGENTIAPPUNTAMENTI ADD FOREIGN KEY (CODAGENTE) REFERENCES PUBLIC.AGENTI(CODAGENTE);
ALTER TABLE PUBLIC.AGENTIAPPUNTAMENTI ADD FOREIGN KEY (CODAPPUNTAMENTO) REFERENCES PUBLIC.APPUNTAMENTI(CODAPPUNTAMENTO);
ALTER TABLE PUBLIC.AGENTIAPPUNTAMENTI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.ALLEGATICOLLOQUIO A SET CODCOLLOQUIO=NULL WHERE (SELECT COUNT(CODCOLLOQUIO) FROM PUBLIC.COLLOQUI WHERE CODCOLLOQUIO = A.CODCOLLOQUIO) = 0;
UPDATE PUBLIC.ALLEGATICOLLOQUIO A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.ALLEGATICOLLOQUIO ADD FOREIGN KEY (CODCOLLOQUIO) REFERENCES PUBLIC.COLLOQUI(CODCOLLOQUIO);
ALTER TABLE PUBLIC.ALLEGATICOLLOQUIO ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.ALLEGATIIMMOBILI A SET CODIMMOBILE=NULL WHERE (SELECT COUNT(CODIMMOBILE) FROM IMMOBILI WHERE CODIMMOBILE = A.CODIMMOBILE) = 0;
UPDATE PUBLIC.ALLEGATIIMMOBILI A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.ALLEGATIIMMOBILI ADD FOREIGN KEY (CODIMMOBILE) REFERENCES PUBLIC.IMMOBILI(CODIMMOBILE);
ALTER TABLE PUBLIC.ALLEGATIIMMOBILI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.ANAGRAFICHE A SET CODAGENTEINSERITORE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODAGENTEINSERITORE) = 0;
UPDATE PUBLIC.ANAGRAFICHE A SET CODCLASSECLIENTE=NULL WHERE (SELECT COUNT(CODCLASSECLIENTE) FROM PUBLIC.CLASSICLIENTE WHERE CODCLASSECLIENTE = A.CODCLASSECLIENTE) = 0;
UPDATE PUBLIC.ANAGRAFICHE A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.ANAGRAFICHE ADD FOREIGN KEY (CODAGENTEINSERITORE) REFERENCES PUBLIC.AGENTI(CODAGENTE);
ALTER TABLE PUBLIC.ANAGRAFICHE ADD FOREIGN KEY (CODCLASSECLIENTE) REFERENCES PUBLIC.CLASSICLIENTE(CODCLASSECLIENTE);
ALTER TABLE PUBLIC.ANAGRAFICHE ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.ANAGRAFICHEAPPUNTAMENTI A SET CODANAGRAFICA=NULL WHERE (SELECT COUNT(CODANAGRAFICA) FROM PUBLIC.ANAGRAFICHE WHERE CODANAGRAFICA = A.CODANAGRAFICA) = 0; 
UPDATE PUBLIC.ANAGRAFICHEAPPUNTAMENTI A SET CODAPPUNTAMENTO=NULL WHERE (SELECT COUNT(CODAPPUNTAMENTO) FROM PUBLIC.APPUNTAMENTI WHERE CODAPPUNTAMENTO = A.CODAPPUNTAMENTO) = 0; 
UPDATE PUBLIC.ANAGRAFICHEAPPUNTAMENTI A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.ANAGRAFICHEAPPUNTAMENTI ADD FOREIGN KEY (CODANAGRAFICA) REFERENCES PUBLIC.ANAGRAFICHE(CODANAGRAFICA);
ALTER TABLE PUBLIC.ANAGRAFICHEAPPUNTAMENTI ADD FOREIGN KEY (CODAPPUNTAMENTO) REFERENCES PUBLIC.APPUNTAMENTI(CODAPPUNTAMENTO);
ALTER TABLE PUBLIC.ANAGRAFICHEAPPUNTAMENTI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.APPUNTAMENTI A SET CODTIPOAPPUNTAMENTO=NULL WHERE (SELECT COUNT(CODTIPOAPPUNTAMENTO) FROM PUBLIC.TIPIAPPUNTAMENTI WHERE CODTIPOAPPUNTAMENTO = A.CODTIPOAPPUNTAMENTO) = 0; 
UPDATE PUBLIC.APPUNTAMENTI A SET CODPADRE=NULL WHERE CODPADRE = 0; 
UPDATE PUBLIC.APPUNTAMENTI A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.APPUNTAMENTI ADD FOREIGN KEY (CODTIPOAPPUNTAMENTO) REFERENCES PUBLIC.TIPIAPPUNTAMENTI(CODTIPOAPPUNTAMENTO);
ALTER TABLE PUBLIC.APPUNTAMENTI ADD FOREIGN KEY (CODPADRE) REFERENCES PUBLIC.APPUNTAMENTI(CODAPPUNTAMENTO);
ALTER TABLE PUBLIC.APPUNTAMENTI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC."ATTRIBUTE" A SET IDCLASSENTITY=NULL WHERE (SELECT COUNT(IDCLASSENTITY) FROM PUBLIC.ENTITY WHERE IDCLASSENTITY = A.IDCLASSENTITY) = 0; 
UPDATE PUBLIC."ATTRIBUTE" A SET LINKEDIDENTITY=NULL WHERE (SELECT COUNT(IDCLASSENTITY) FROM PUBLIC.ENTITY WHERE IDCLASSENTITY = A.LINKEDIDENTITY) = 0;
UPDATE PUBLIC."ATTRIBUTE" A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC."ATTRIBUTE" ADD FOREIGN KEY (IDCLASSENTITY) REFERENCES PUBLIC.ENTITY(IDCLASSENTITY);
ALTER TABLE PUBLIC."ATTRIBUTE" ADD FOREIGN KEY (LINKEDIDENTITY) REFERENCES PUBLIC.ENTITY(IDCLASSENTITY);
ALTER TABLE PUBLIC."ATTRIBUTE" ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.ATTRIBUTEVALUE A SET IDFIELD=NULL WHERE (SELECT COUNT(IDATTRIBUTE) FROM PUBLIC."ATTRIBUTE" WHERE IDATTRIBUTE = A.IDFIELD) = 0; 
UPDATE PUBLIC.ATTRIBUTEVALUE A SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = A.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.ATTRIBUTEVALUE ADD FOREIGN KEY (IDFIELD) REFERENCES PUBLIC."ATTRIBUTE"(IDATTRIBUTE);
ALTER TABLE PUBLIC.ATTRIBUTEVALUE ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.CLASSICLIENTE C SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = C.CODUSERUPDATE) = 0; 
ALTER TABLE PUBLIC.CLASSICLIENTE ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.CLASSIENERGETICHE C SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = C.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.CLASSIENERGETICHE ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.COLLOQUI C SET CODAGENTEINSERITORE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = C.CODUSERUPDATE) = 0;
UPDATE PUBLIC.COLLOQUI C SET CODIMMOBILEABBINATO=NULL WHERE (SELECT COUNT(CODIMMOBILE) FROM IMMOBILI WHERE CODIMMOBILE = C.CODIMMOBILEABBINATO) = 0;
UPDATE PUBLIC.COLLOQUI SET CODTIPOLOGIACOLLOQUIO=NULL WHERE CODTIPOLOGIACOLLOQUIO=0;
UPDATE PUBLIC.COLLOQUI C SET CODPARENT=NULL WHERE (SELECT COUNT(CODCOLLOQUIO) FROM PUBLIC.COLLOQUI WHERE CODCOLLOQUIO = C.CODPARENT) = 0;
UPDATE PUBLIC.COLLOQUI C SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = C.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.COLLOQUI ADD FOREIGN KEY (CODAGENTEINSERITORE) REFERENCES PUBLIC.AGENTI(CODAGENTE);
ALTER TABLE PUBLIC.COLLOQUI ADD FOREIGN KEY (CODIMMOBILEABBINATO) REFERENCES PUBLIC.IMMOBILI(CODIMMOBILE);
ALTER TABLE PUBLIC.COLLOQUI ADD FOREIGN KEY (CODPARENT) REFERENCES PUBLIC.COLLOQUI(CODCOLLOQUIO);
ALTER TABLE PUBLIC.COLLOQUI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.COLLOQUIAGENTI C SET CODAGENTE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = C.CODAGENTE) = 0;
UPDATE PUBLIC.COLLOQUIAGENTI C SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = C.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.COLLOQUIAGENTI ADD FOREIGN KEY (CODAGENTE) REFERENCES PUBLIC.AGENTI(CODAGENTE);
ALTER TABLE PUBLIC.COLLOQUIAGENTI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.COLLOQUIANAGRAFICHE C SET CODCOLLOQUIO=NULL WHERE (SELECT COUNT(CODCOLLOQUIO) FROM PUBLIC.COLLOQUI WHERE CODCOLLOQUIO = C.CODCOLLOQUIO) = 0; 
UPDATE PUBLIC.COLLOQUIANAGRAFICHE C SET CODANAGRAFICA=NULL WHERE (SELECT COUNT(CODANAGRAFICA) FROM PUBLIC.ANAGRAFICHE WHERE CODANAGRAFICA = C.CODANAGRAFICA) = 0;
UPDATE PUBLIC.COLLOQUIANAGRAFICHE C SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = C.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.COLLOQUIANAGRAFICHE ADD FOREIGN KEY (CODCOLLOQUIO) REFERENCES PUBLIC.COLLOQUI(CODCOLLOQUIO);
ALTER TABLE PUBLIC.COLLOQUIANAGRAFICHE ADD FOREIGN KEY (CODANAGRAFICA) REFERENCES PUBLIC.ANAGRAFICHE(CODANAGRAFICA);
ALTER TABLE PUBLIC.COLLOQUIANAGRAFICHE ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.COLLOQUICRITERIRICERCA C SET CODCOLLOQUIO=NULL WHERE (SELECT COUNT(CODCOLLOQUIO) FROM PUBLIC.COLLOQUI WHERE CODCOLLOQUIO = C.CODCOLLOQUIO) = 0;
UPDATE PUBLIC.COLLOQUICRITERIRICERCA C SET CODRICERCA=NULL WHERE (SELECT COUNT(CODRICERCA) FROM PUBLIC.RICERCHE WHERE CODRICERCA = C.CODRICERCA) = 0;
UPDATE PUBLIC.COLLOQUICRITERIRICERCA C SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = C.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.COLLOQUICRITERIRICERCA ADD FOREIGN KEY (CODCOLLOQUIO) REFERENCES PUBLIC.COLLOQUI(CODCOLLOQUIO);
ALTER TABLE PUBLIC.COLLOQUICRITERIRICERCA ADD FOREIGN KEY (CODRICERCA) REFERENCES PUBLIC.RICERCHE(CODRICERCA);
ALTER TABLE PUBLIC.COLLOQUICRITERIRICERCA ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.CONTATTI C SET CODTIPOLOGIACONTATTO=NULL WHERE (SELECT COUNT(CODTIPOLOGIACONTATTO) FROM PUBLIC.TIPOLOGIECONTATTI WHERE CODTIPOLOGIACONTATTO = C.CODTIPOLOGIACONTATTO) = 0; 
UPDATE PUBLIC.CONTATTI C SET CODANAGRAFICA=NULL WHERE (SELECT COUNT(CODANAGRAFICA) FROM PUBLIC.ANAGRAFICHE WHERE CODANAGRAFICA = C.CODANAGRAFICA) = 0;
UPDATE PUBLIC.CONTATTI C SET CODAGENTE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = C.CODAGENTE) = 0;
UPDATE PUBLIC.CONTATTI C SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = C.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.CONTATTI ADD FOREIGN KEY (CODTIPOLOGIACONTATTO) REFERENCES PUBLIC.TIPOLOGIECONTATTI(CODTIPOLOGIACONTATTO);
ALTER TABLE PUBLIC.CONTATTI ADD FOREIGN KEY (CODANAGRAFICA) REFERENCES PUBLIC.ANAGRAFICHE(CODANAGRAFICA);
ALTER TABLE PUBLIC.CONTATTI ADD FOREIGN KEY (CODAGENTE) REFERENCES PUBLIC.AGENTI(CODAGENTE);
ALTER TABLE PUBLIC.CONTATTI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.DATICATASTALI D SET CODIMMOBILE=NULL WHERE (SELECT COUNT(CODIMMOBILE) FROM IMMOBILI WHERE CODIMMOBILE = D.CODIMMOBILE) = 0;
UPDATE PUBLIC.DATICATASTALI D SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = D.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.DATICATASTALI ADD FOREIGN KEY (CODIMMOBILE) REFERENCES PUBLIC.IMMOBILI(CODIMMOBILE);
ALTER TABLE PUBLIC.DATICATASTALI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.ENTITY E SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = E.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.ENTITY ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.GCALENDAR G SET CODGDATA=NULL WHERE (SELECT COUNT(CODGDATA) FROM PUBLIC.GDATA WHERE CODGDATA =G.CODGDATA) = 0; 
UPDATE PUBLIC.GCALENDAR G SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = G.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.GCALENDAR ADD FOREIGN KEY (CODGDATA) REFERENCES PUBLIC.GDATA(CODGDATA);
ALTER TABLE PUBLIC.GCALENDAR ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.GDATA G SET CODCONTATTO=NULL WHERE (SELECT COUNT(CODCONTATTO) FROM PUBLIC.CONTATTI WHERE CODCONTATTO = G.CODCONTATTO) = 0;
UPDATE PUBLIC.GDATA G SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = G.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.GDATA ADD FOREIGN KEY (CODCONTATTO) REFERENCES PUBLIC.CONTATTI(CODCONTATTO);
ALTER TABLE PUBLIC.GDATA ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.IMMAGINI I SET CODIMMOBILE=NULL WHERE (SELECT COUNT(CODIMMOBILE) FROM IMMOBILI WHERE CODIMMOBILE = I.CODIMMOBILE) = 0;
UPDATE PUBLIC.IMMAGINI I SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = I.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.IMMAGINI ADD FOREIGN KEY (CODIMMOBILE) REFERENCES PUBLIC.IMMOBILI(CODIMMOBILE);
ALTER TABLE PUBLIC.IMMAGINI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.IMMOBILI I SET CODANAGRAFICA=NULL WHERE (SELECT COUNT(CODANAGRAFICA) FROM PUBLIC.ANAGRAFICHE WHERE CODANAGRAFICA = I.CODANAGRAFICA) = 0;
UPDATE PUBLIC.IMMOBILI I SET CODSTATO=NULL WHERE (SELECT COUNT(CODSTATOCONSERVATIVO) FROM PUBLIC.STATOCONSERVATIVO WHERE CODSTATOCONSERVATIVO = I.CODSTATO) = 0; 
UPDATE PUBLIC.IMMOBILI I SET CODTIPOLOGIA=NULL WHERE (SELECT COUNT(CODTIPOLOGIAIMMOBILE) FROM PUBLIC.TIPOLOGIEIMMOBILI WHERE CODTIPOLOGIAIMMOBILE = I.CODTIPOLOGIA) = 0;
UPDATE PUBLIC.IMMOBILI I SET CODRISCALDAMENTO=NULL WHERE (SELECT COUNT(CODRISCALDAMENTO) FROM PUBLIC.RISCALDAMENTI WHERE CODRISCALDAMENTO = I.CODRISCALDAMENTO) = 0;
UPDATE PUBLIC.IMMOBILI I SET CODAGENTEINSERITORE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = I.CODAGENTEINSERITORE) = 0;
UPDATE PUBLIC.IMMOBILI I SET CODCLASSEENERGETICA=NULL WHERE (SELECT COUNT(CODCLASSEENERGETICA) FROM PUBLIC.CLASSIENERGETICHE WHERE CODCLASSEENERGETICA = I.CODCLASSEENERGETICA) = 0;
UPDATE PUBLIC.IMMOBILI I SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = I.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.IMMOBILI ADD FOREIGN KEY (CODANAGRAFICA) REFERENCES PUBLIC.ANAGRAFICHE(CODANAGRAFICA);
ALTER TABLE PUBLIC.IMMOBILI ADD FOREIGN KEY (CODSTATO) REFERENCES PUBLIC.STATOCONSERVATIVO(CODSTATOCONSERVATIVO);
ALTER TABLE PUBLIC.IMMOBILI ADD FOREIGN KEY (CODTIPOLOGIA) REFERENCES PUBLIC.TIPOLOGIEIMMOBILI(CODTIPOLOGIAIMMOBILE);
ALTER TABLE PUBLIC.IMMOBILI ADD FOREIGN KEY (CODRISCALDAMENTO) REFERENCES PUBLIC.RISCALDAMENTI(CODRISCALDAMENTO);
ALTER TABLE PUBLIC.IMMOBILI ADD FOREIGN KEY (CODAGENTEINSERITORE) REFERENCES PUBLIC.AGENTI(CODAGENTE);
ALTER TABLE PUBLIC.IMMOBILI ADD FOREIGN KEY (CODCLASSEENERGETICA) REFERENCES PUBLIC.CLASSIENERGETICHE(CODCLASSEENERGETICA);
ALTER TABLE PUBLIC.IMMOBILI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.IMMOBILIPROPIETARI I SET CODIMMOBILE=NULL WHERE (SELECT COUNT(CODIMMOBILE) FROM IMMOBILI WHERE CODIMMOBILE = I.CODIMMOBILE) = 0;
UPDATE PUBLIC.IMMOBILIPROPIETARI I SET CODANAGRAFICA=NULL WHERE (SELECT COUNT(CODANAGRAFICA) FROM PUBLIC.ANAGRAFICHE WHERE CODANAGRAFICA = I.CODANAGRAFICA) = 0;
DELETE FROM PUBLIC.IMMOBILIPROPIETARI WHERE CODIMMOBILE IS NULL AND CODANAGRAFICA IS NULL;
ALTER TABLE PUBLIC.IMMOBILIPROPIETARI ADD FOREIGN KEY (CODIMMOBILE) REFERENCES PUBLIC.IMMOBILI(CODIMMOBILE);
ALTER TABLE PUBLIC.IMMOBILIPROPIETARI ADD FOREIGN KEY (CODANAGRAFICA) REFERENCES PUBLIC.ANAGRAFICHE(CODANAGRAFICA);

UPDATE PUBLIC.PERMESSI P SET CODAGENTE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = P.CODAGENTE) = 0;
UPDATE PUBLIC.PERMESSI P SET CODRICERCA=NULL WHERE (SELECT COUNT(CODRICERCA) FROM PUBLIC.RICERCHE WHERE CODRICERCA = P.CODRICERCA) = 0;
UPDATE PUBLIC.PERMESSI P SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = P.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.PERMESSI ADD FOREIGN KEY (CODAGENTE) REFERENCES PUBLIC.AGENTI(CODAGENTE);
ALTER TABLE PUBLIC.PERMESSI ADD FOREIGN KEY (CODRICERCA) REFERENCES PUBLIC.RICERCHE(CODRICERCA);
ALTER TABLE PUBLIC.PERMESSI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.PERMESSIUI P SET CODAGENTE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = P.CODAGENTE) = 0;
UPDATE PUBLIC.PERMESSIUI P SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = P.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.PERMESSIUI ADD FOREIGN KEY (CODAGENTE) REFERENCES PUBLIC.AGENTI(CODAGENTE);
ALTER TABLE PUBLIC.PERMESSIUI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.PROMEMORIA P SET CODAGENTE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = P.CODAGENTE) = 0;
UPDATE PUBLIC.PROMEMORIA P SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = P.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.PROMEMORIA ADD FOREIGN KEY (CODAGENTE) REFERENCES PUBLIC.AGENTI(CODAGENTE);
ALTER TABLE PUBLIC.PROMEMORIA ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.PROMEMORIAOGGETTI SET CODOGGETTO=NULL WHERE CODOGGETTO=0;

UPDATE PUBLIC.REPORT R SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = R.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.REPORT ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.REPORTMARKERS R SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = R.CODUSERUPDATE) = 0;
UPDATE PUBLIC.REPORTMARKERS R SET CODREPORT=NULL WHERE (SELECT COUNT(CODREPORT) FROM PUBLIC.REPORT WHERE CODREPORT = R.CODREPORT) = 0;
ALTER TABLE PUBLIC.REPORTMARKERS ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);
ALTER TABLE PUBLIC.REPORTMARKERS ADD FOREIGN KEY (CODREPORT) REFERENCES PUBLIC.REPORT(CODREPORT);

UPDATE PUBLIC.RICERCHE R SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = R.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.RICERCHE ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.RISCALDAMENTI R SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = R.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.RISCALDAMENTI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.STANZEIMMOBILI S SET CODIMMOBILE=NULL WHERE (SELECT COUNT(CODIMMOBILE) FROM IMMOBILI WHERE CODIMMOBILE = S.CODIMMOBILE) = 0;
UPDATE PUBLIC.STANZEIMMOBILI S SET CODTIPOLOGIASTANZA=NULL WHERE (SELECT COUNT(CODTIPOLOGIASTANZE) FROM TIPOLOGIASTANZE WHERE CODTIPOLOGIASTANZE = S.CODTIPOLOGIASTANZA) = 0;
UPDATE PUBLIC.STANZEIMMOBILI S SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = S.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.STANZEIMMOBILI ADD FOREIGN KEY (CODIMMOBILE) REFERENCES PUBLIC.IMMOBILI(CODIMMOBILE);
ALTER TABLE PUBLIC.STANZEIMMOBILI ADD FOREIGN KEY (CODTIPOLOGIASTANZA) REFERENCES PUBLIC.TIPOLOGIASTANZE(CODTIPOLOGIASTANZE);
ALTER TABLE PUBLIC.STANZEIMMOBILI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.STATOCONSERVATIVO S SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = S.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.STATOCONSERVATIVO ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.TIPIAPPUNTAMENTI T SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = T.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.TIPIAPPUNTAMENTI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.TIPOLOGIASTANZE T SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = T.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.TIPOLOGIASTANZE ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.TIPOLOGIECOLLOQUI T SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = T.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.TIPOLOGIECOLLOQUI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.TIPOLOGIECONTATTI T SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = T.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.TIPOLOGIECONTATTI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.TIPOLOGIEIMMOBILI T SET CODUSERUPDATE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = T.CODUSERUPDATE) = 0;
ALTER TABLE PUBLIC.TIPOLOGIEIMMOBILI ADD FOREIGN KEY (CODUSERUPDATE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

UPDATE PUBLIC.WINKGCALENDAR W SET CODCOLLOQUIO=NULL WHERE (SELECT COUNT(CODCOLLOQUIO) FROM PUBLIC.COLLOQUI WHERE CODCOLLOQUIO = W.CODCOLLOQUIO) = 0;
UPDATE PUBLIC.WINKGCALENDAR W SET CODAPPUNTAMENTO=NULL WHERE (SELECT COUNT(CODAPPUNTAMENTO) FROM APPUNTAMENTI WHERE CODAPPUNTAMENTO = W.CODAPPUNTAMENTO) = 0; 
UPDATE PUBLIC.WINKGCALENDAR W SET CODAGENTE=NULL WHERE (SELECT COUNT(CODAGENTE) FROM PUBLIC.AGENTI WHERE CODAGENTE = W.CODAGENTE) = 0;
ALTER TABLE PUBLIC.WINKGCALENDAR ADD FOREIGN KEY (CODCOLLOQUIO) REFERENCES PUBLIC.COLLOQUI(CODCOLLOQUIO);
ALTER TABLE PUBLIC.WINKGCALENDAR ADD FOREIGN KEY (CODAPPUNTAMENTO) REFERENCES PUBLIC.APPUNTAMENTI(CODAPPUNTAMENTO);
ALTER TABLE PUBLIC.WINKGCALENDAR ADD FOREIGN KEY (CODAGENTE) REFERENCES PUBLIC.AGENTI(CODAGENTE);

CREATE MEMORY TABLE PUBLIC.PROMEMORIALINKS(CODPROMEMORIA INTEGER, DESCRIZIONE VARCHAR(255), URILINK VARCHAR(1048576),ISFILE BOOLEAN DEFAULT FALSE);
CREATE INDEX PROMEMORIA_LINKS ON PUBLIC.PROMEMORIALINKS(CODPROMEMORIA);
ALTER TABLE PUBLIC.PROMEMORIALINKS ADD FOREIGN KEY (CODPROMEMORIA) REFERENCES PUBLIC.PROMEMORIA(CODPROMEMORIA);

