CREATE MEMORY TABLE PUBLIC.IMMOBILIPROPIETARI(CODIMMOBILE INTEGER, CODANAGRAFICA INTEGER);
CREATE UNIQUE INDEX IMMOBILIPROPIETARI_CODS ON PUBLIC.IMMOBILIPROPIETARI (CODIMMOBILE,CODANAGRAFICA);

INSERT INTO IMMOBILIPROPIETARI SELECT CODIMMOBILE,CODANAGRAFICA from IMMOBILI;