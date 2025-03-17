-- Kustutab public schema (mis põhimõtteliselt kustutab kõik tabelid)
DROP SCHEMA IF EXISTS game CASCADE;
-- Loob uue public schema vajalikud õigused
CREATE SCHEMA game
-- taastab vajalikud andmebaasi õigused
    GRANT ALL ON SCHEMA game TO postgres;
GRANT ALL ON SCHEMA game TO PUBLIC;