--liquibase formatted sql
--changeset doc:015

drop database if exists "doc-keycloak-db";
CREATE DATABASE "doc-keycloak-db";
