-- ===============================================
-- Flyway Migration V1
-- Create users and roles tables with PK
-- ===============================================

-- Users table
CREATE TABLE public.users
(
    id                         UUID PRIMARY KEY,
    username                   VARCHAR(255) NOT NULL,
    password                   VARCHAR(255),
    is_account_non_expired     BOOLEAN      NOT NULL,
    is_account_non_locked      BOOLEAN      NOT NULL,
    is_credentials_non_expired BOOLEAN      NOT NULL,
    is_enabled                 BOOLEAN      NOT NULL
);

-- Custom user roles table
CREATE TABLE public.custom_user_roles
(
    custom_user_id UUID         NOT NULL,
    roles          VARCHAR(255) NOT NULL,
    CONSTRAINT custom_user_roles_roles_check CHECK (roles IN ('GUEST', 'USER', 'ADMIN')),
    CONSTRAINT custom_user_roles_pkey PRIMARY KEY (custom_user_id, roles),
    CONSTRAINT custom_user_roles_user_fk FOREIGN KEY (custom_user_id)
        REFERENCES public.users (id) ON DELETE CASCADE
);

-- Flyway schema history table will be created automatically by Flyway
