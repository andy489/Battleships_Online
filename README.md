# Battleships-Online
Java Console Game. MJT Project @ FMI

- [PROJECT DESCRIPTION](https://github.com/andy489/Modern_Java_Technologies/blob/main/course-projects/battleships.md)

<strong>
  
- [DOCUMENTATION LINK](https://github.com/andy489/Battleships_Online/blob/main/Documentation/BSO%20EN.pdf)
  
</strong>

## Testing

<div align="center">
  
![](https://img.shields.io/badge/Code%20Coverage-Class%2089%25%2C%20Method%2090%25%2C%20Line%2084%25-brightgreen) &nbsp; &nbsp;
![](https://img.shields.io/badge/Tests-183%20passed%2C%200%20failed-brightgreen)

</div>

## Functionalities

### User manual

Formats and displays the on-line commands. User manual is divided into two sections, game mode and non-game mode. Depending on the mode in which the user is, the usual commands will be displayed.

```
man
```

### Who am I

Display message containing effective user username.

```
who-am-i
```

### Set username

Attaches username to the user id on the server.

```
set-username <username>
```

### Change username

Changes the current username of the user.

```
change-username <username>
```

### List users

Displays the total number of users currently connected to the server on the first line and on each subsequent line shows the username and status of each user.

```
list-users
```

### List games

Displays a table with information about the available games that are currently being played and served by the server.

```
list-games
```

### Register

Registers a user on the server to allow him to create (host) a game or join as a second (guest) player in already created games. Execution of the command returns a unique api-key with which the user can be identified on the server when logging in again.

```
register
```

### Login

Identifies a pre-registered user on the server. All saved games are loaded on the server in the RAM reserved for this user.

```
login <api-key>
```

### Create game

Creates a new game with the submitted name, in which the creator is host. The status of the game is pending and a second player is waited to join the game. Boats can be placed while waiting (see place commands below)

```
create-game <game_name>
```

### Join game

Command with one optional parameter. In case this optional parameter is missing, the user joins any pending game. If the argument is given, the player tries to join the game with the name given as an argument.

```
join-game {<game_name>}
```

### Current game

Returns the current game to a user who is registered and is currently in a game as creator or guest.

```
current-game
```

### Save game

Saves the current game of the user who calls it. The command can only be called if the user's current game is in progress.

```
save-game
```

### Saved games

Displays a table of the currently saved games of a registered user.

```
saved-games
```

### Delete game

Command with one required argument and several optional ones. All arguments are names of games to be deleted. Server tries to delete each game with the given name if it finds such a game in the games saved by the user.

```
delete-game <game_name_1> {<game_name_2> <game_name_3> ... <game_name_n>}
```

### Place

Places a battleship on the board. Case-insensitive.

```
place <[A-J]> <[1-10]> <[UP, RIGHT, DOWN, LEFT]> <[2-5]>
```

### Place all

Places all battleships stochastically.

```
place-all
```

### Start

Indicates that the opponent is ready to start attacking (he has already placed all his ships on the board). Case-insensitive.

```
start
```

### Attack

Attacks at a position corresponding to the coordinates given as arguments.

```
attack <[A-J]> <[1-10]>
```

### Cheats manual

Displays a usage message of a hidden command.

```
hacks
```

### Attack all

Attacks the lines that correspond to the letters given as arguments. Case-insensitive.

```
attack-all <[A-J]> {<[A-J]> ... }
```

### Display

Displays the state of the boards of the current game.

```
display
```

### Disconnect

Disconnects the user from the server.

```
disconnect
``` 

## Specifics

### Username

Тhe username is validated with a regular expression must be no less than 4 and no more than 9 lowercase or uppercase Latin letters.

### Player status

* ONLINE
* HOST
* GUEST

### Game Status

* PENDING
* IN PROGRESS

## Structure

```
src
└─ bg.sofia.uni.fmi.mjt.battleships.online.server
    ├── command
    │     ├── manager 
    │     │    ├── Command.java
    │     │    ├── CommandCreator.java
    │     │    └── CommandExecutor.java
    │     ├── mode
    │     │     ├── playing
    │     │     │     ├── attack
    │     │     │     │     ├── Attack.java
    │     │     │     │     └── AttackAll.java
    │     │     │     │── placement     
    │     │     │     │     ├── Place.java
    │     │     │     │     └── PlaceAll.java
    │     │     │     ├── DeleteGame.java
    │     │     │     ├── Hacks.java
    │     │     │     ├── SaveGame.java
    │     │     │     └── Start.java
    │     │     ├── surfing
    │     │     │     ├── ChangeUsername.java
    │     │     │     ├── CreateGame.java
    │     │     │     ├── CurrentGame.java
    │     │     │     ├── JoinGame.java
    │     │     │     ├── LoadGame.java
    │     │     │     ├── Login.java
    │     │     │     ├── Register.java
    │     │     │     ├── SavedGames.java
    │     │     │     └── SetUsername.java
    │     │     ├── view     
    │     │     │     ├── Display.java
    │     │     │     ├── ListGames.java
    │     │     │     ├── ListUsers.java
    │     │     │     └──  WhoAmI.java
    │     │     ├── Disconnect.java   
    │     │     └──  UserManual.java    
    │     └── response
    │           ├── AttackResponseDTO.java   
    │           ├── CommandResponseDTO.java   
    │           ├── MessageJson.java   
    │           ├── PlaceResponseDTO.java   
    │           └── SaveResponseDTO.java   
    ├── exception
    │     └── Log.java
    ├── file.manager
    │     └── FileManager.java
    ├── game
    │     ├── player    
    │     │     ├── board  
    │     │     │     ├── field  
    │     │     │     │     ├── ship   
    │     │     │     │     │     ├── coordinate   
    │     │     │     │     │     │     └── Coordinate.java   
    │     │     │     │     │     ├── Ship.java  
    │     │     │     │     │     ├── ShipConstants.java    
    │     │     │     │     │     ├── ShipDirection.java    
    │     │     │     │     │     └── ShipType.java  
    │     │     │     │     ├── Field.java    
    │     │     │     │     ├── FieldConstants.java          
    │     │     │     │     └── FieldType.java
    │     │     │     ├── Board.java    
    │     │     │     └── BoardConstants.java
    │     │     ├── Player.java
    │     │     └── PlayerStatus.java
    │     ├── Game.java
    │     ├── GameStatus.java
    │     └── SavedGame.java
    ├── storage
    │     ├── DisplayGameHelper.java
    │     ├── ListGamesViewHelper.java
    │     ├── SetUpRegisteredUsersAndSavedGames.java
    │     ├── Storage.java
    │     └── UserIn.java
    └── BattleshipsServer.java  
```
