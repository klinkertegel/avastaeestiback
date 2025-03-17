-- Created by Vertabelo (http://vertabelo.com)
-- Last modification date: 2025-03-07 07:59:53.693

-- tables
-- Table: favourite
CREATE TABLE favourite (
                           id serial  NOT NULL,
                           user_id int  NOT NULL,
                           location_id int  NOT NULL,
                           status char(1)  NOT NULL,
                           CONSTRAINT favourite_pk PRIMARY KEY (id)
);

-- Table: game
CREATE TABLE game (
                      id serial  NOT NULL,
                      user_id int  NOT NULL,
                      name varchar(255)  NOT NULL,
                      description varchar(1000)  NOT NULL,
                      time_per_location int  NOT NULL,
                      status varchar(1)  NOT NULL,
                      CONSTRAINT id PRIMARY KEY (id)
);

-- Table: game_location
CREATE TABLE game_location (
                               id serial  NOT NULL,
                               game_id int  NOT NULL,
                               location_id int  NOT NULL,
                               CONSTRAINT game_location_pk PRIMARY KEY (id)
);

-- Table: leader_board
CREATE TABLE leader_board (
                              id serial  NOT NULL,
                              game_id int  NOT NULL,
                              user_id int  NOT NULL,
                              timestamp timestamp  NOT NULL,
                              total_score int  NOT NULL,
                              CONSTRAINT leader_board_pk PRIMARY KEY (id)
);

-- Table: location
CREATE TABLE location (
                          id serial  NOT NULL,
                          name varchar(255)  NOT NULL,
                          longitude decimal(10,8)  NOT NULL,
                          latitude decimal(10,8)  NOT NULL,
                          clue varchar(1000)  NOT NULL,
                          status varchar(1)  NOT NULL,
                          image_data bytea  NOT NULL,
                          CONSTRAINT location_pk PRIMARY KEY (id)
);

-- Table: random_game
CREATE TABLE random_game (
                             id serial  NOT NULL,
                             user_id int  NOT NULL,
                             total_locations int  NOT NULL,
                             locations_answered int  NOT NULL,
                             is_complete boolean  NOT NULL,
                             CONSTRAINT random_game_pk PRIMARY KEY (id)
);

-- Table: random_game_location
CREATE TABLE random_game_location (
                                      id serial  NOT NULL,
                                      random_game_id int  NOT NULL,
                                      location_id int  NOT NULL,
                                      is_correct boolean  NOT NULL,
                                      state varchar(2)  NOT NULL,
                                      time_start timestamp  NULL,
                                      time_end timestamp  NULL,
                                      CONSTRAINT random_game_location_pk PRIMARY KEY (id)
);

-- Table: role
CREATE TABLE role (
                      id serial  NOT NULL,
                      role_name varchar(255)  NOT NULL,
                      CONSTRAINT role_pk PRIMARY KEY (id)
);

-- Table: user
CREATE TABLE "user" (
                        id serial  NOT NULL,
                        role_id int  NOT NULL,
                        email varchar(255)  NOT NULL,
                        username varchar(20)  NOT NULL,
                        password varchar(20)  NOT NULL,
                        status char(1)  NOT NULL,
                        CONSTRAINT user_ak_1 UNIQUE (username) NOT DEFERRABLE  INITIALLY IMMEDIATE,
                        CONSTRAINT user_pk PRIMARY KEY (id)
);

-- Table: user_game
CREATE TABLE user_game (
                           id serial  NOT NULL,
                           user_id int  NOT NULL,
                           game_id int  NOT NULL,
                           total_locations int  NOT NULL,
                           locations_answered int  NOT NULL,
                           total_score int  NOT NULL,
                           correct_answers int  NOT NULL,
                           is_complete boolean  NOT NULL,
                           CONSTRAINT user_game_pk PRIMARY KEY (id)
);

-- Table: user_game_location
CREATE TABLE user_game_location (
                                    id serial  NOT NULL,
                                    user_game_id int  NOT NULL,
                                    location_id int  NOT NULL,
                                    is_correct boolean  NOT NULL,
                                    state varchar(2)  NOT NULL,
                                    time_start timestamp  NULL,
                                    time_end timestamp  NULL,
                                    game_id int  NOT NULL,
                                    user_id int  NOT NULL,
                                    CONSTRAINT user_game_location_pk PRIMARY KEY (id)
);

-- foreign keys
-- Reference: favourite_location (table: favourite)
ALTER TABLE favourite ADD CONSTRAINT favourite_location
    FOREIGN KEY (location_id)
        REFERENCES location (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: favourite_user (table: favourite)
ALTER TABLE favourite ADD CONSTRAINT favourite_user
    FOREIGN KEY (user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: game_question_game (table: game_location)
ALTER TABLE game_location ADD CONSTRAINT game_question_game
    FOREIGN KEY (game_id)
        REFERENCES game (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: game_question_location (table: game_location)
ALTER TABLE game_location ADD CONSTRAINT game_question_location
    FOREIGN KEY (location_id)
        REFERENCES location (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: game_user (table: game)
ALTER TABLE game ADD CONSTRAINT game_user
    FOREIGN KEY (user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: random_game_questions_location (table: random_game_location)
ALTER TABLE random_game_location ADD CONSTRAINT random_game_questions_location
    FOREIGN KEY (location_id)
        REFERENCES location (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: random_game_questions_random_game (table: random_game_location)
ALTER TABLE random_game_location ADD CONSTRAINT random_game_questions_random_game
    FOREIGN KEY (random_game_id)
        REFERENCES random_game (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: random_game_user (table: random_game)
ALTER TABLE random_game ADD CONSTRAINT random_game_user
    FOREIGN KEY (user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: score_board_game (table: leader_board)
ALTER TABLE leader_board ADD CONSTRAINT score_board_game
    FOREIGN KEY (game_id)
        REFERENCES game (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: score_board_user (table: leader_board)
ALTER TABLE leader_board ADD CONSTRAINT score_board_user
    FOREIGN KEY (user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: user_game_game (table: user_game)
ALTER TABLE user_game ADD CONSTRAINT user_game_game
    FOREIGN KEY (game_id)
        REFERENCES game (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: user_game_question_game (table: user_game_location)
ALTER TABLE user_game_location ADD CONSTRAINT user_game_question_game
    FOREIGN KEY (game_id)
        REFERENCES game (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: user_game_question_location (table: user_game_location)
ALTER TABLE user_game_location ADD CONSTRAINT user_game_question_location
    FOREIGN KEY (location_id)
        REFERENCES location (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: user_game_question_user (table: user_game_location)
ALTER TABLE user_game_location ADD CONSTRAINT user_game_question_user
    FOREIGN KEY (user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: user_game_question_user_game (table: user_game_location)
ALTER TABLE user_game_location ADD CONSTRAINT user_game_question_user_game
    FOREIGN KEY (user_game_id)
        REFERENCES user_game (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: user_game_user (table: user_game)
ALTER TABLE user_game ADD CONSTRAINT user_game_user
    FOREIGN KEY (user_id)
        REFERENCES "user" (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- Reference: user_role (table: user)
ALTER TABLE "user" ADD CONSTRAINT user_role
    FOREIGN KEY (role_id)
        REFERENCES role (id)
        NOT DEFERRABLE
            INITIALLY IMMEDIATE
;

-- End of file.

