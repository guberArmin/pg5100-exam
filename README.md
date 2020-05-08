[![Build Status](https://travis-ci.com/guberArmin/pg5100-exam.svg?token=m6BpjWymm3UWnZ6QxDwC&branch=master)](https://travis-ci.com/guberArmin/pg5100-exam)
# Exam in Testing, Security and Development of Enterprise Systems

## About
This is simple gotcha game implementation in springboot. This game is inspired by 
hearthstone and cards from that game. But in general it would not be huge problem
to switch it to some other type of gotcha game.
#### Rules
 - Each newly registered user gets 4 loot boxes
 - Each loot box has 3 items
 - Each item is worth 25 ducats (in game currency)
 - To buy loot box you need 100 ducats
 - Cards are sorted by rarity
 - Only duplicates can be sold (discussed later on why)
 - Cards can be golden or not golden. (not worth more, just get golden frame)
 
#### General
- On `home page` you can see 10 cards from database
- On `my collection` page you can see: `cards you own`, `duplicates` (by clicking 
`show duplicates` button) and you can redeem loot boxes, if you have any.
- Content of last redeemed box will be shown on `my collection` page, and will be present
in that session.
- On `Store` page you can: `see your balance`,`check number of loot boxes`, `buy new 
loot box` (if you have enough ducats). Also at this page you can `sell` copies of 
cards you own.
- On `user page` you can see stats about your user and check `missing cards`


## Requierments
All requirements from exam are completed. In addition to those I have done some extra 
things that I am going to discuss later.

## Usage

- To run website run class `LocalApplicationRunner` from module `frontend`, under 
`test/java` in package `no.kristiania.exam.tsdes`. Webpage is found at `localhost:8080`

- To generate coverage with jacoco plugin command `mvn clean verify` is used

## Test coverage

- Frontend: 85%
- Backend: 93%
- Total: 91%

[Screenshoot of jacoco report](./docs/jacoco.png)
## Default data
- I have initialized database with 15 cards.
- In addition to cards there are some other users:
  - **Username**: `kitkat` **Password**: `123123`
  - **Username**: `lollipop` **Password**: `123123`
  - **Username**: `oreo` **Password**: `123123`

## Ambiguities

- R1 `mill/sell a copy of an item for in-game currency `. I was not sure should 
this be implemented that just copies (duplicates) can be sold, or all cards.
I went with implementation where only duplicates are sellable. This decision did
not impact my coding greatly as it does not make much of a difference (from
programming point of view) is last card sellable or not.

- R3 `Own collection page` requirements. I was not sure should all of needed functionalities
should be on one page, or multiple pages. That is why I divided them in two.
First one is `My collection`, where user can see card and loot boxes he owns (and
redeem loot boxes). Second one is `Store` where user can see how much money does he have,
buy new loot boxes and sell copies of items.

## Extras

- To make website more usable and prettier I have used bootstrap, in combination with
some of my own css, to style elements on pages.

- I have added user page and wrote `selenium tests` for it. At this page user can see
stats about how many copies do they own, and how many cards do they posess. Also all missing cards
for given user are displayed. 

- Website is deployed to heroku and can be found at [this link](https://pg5100-exam-2020.herokuapp.com/)
(it can take a bit to start it, as it is free version of heroku)

- For `continuous integration` I have uploaded project to github and deployed it to 
`travis-ci`. As we have to keep our projects private I can not provide direct link to 
travis-ci. As proof of deployment I have provided travis-ci badge at beginning of file
and screenshot of small portion of deployment. I could not take screen shoot of whole
log as my github id is same as my name and last name. Even in this small portion I had 
to redact my username. Screenshot can be found [here](./docs/travis-ci.png) 

## Bugs
- There are no known bugs in application. I did my best to test edge cases and 
to prevent bugs.

- Version on cloud is tested, but not in
so much details as one running on `localhost`. As it runs on cloud it could be expected
that there are some minor differences compared to local version.



golden cards

rarity