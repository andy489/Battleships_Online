package bg.sofia.uni.fmi.mjt.battleships.online.server.command.mode.view;

import bg.sofia.uni.fmi.mjt.battleships.online.server.command.response.CommandResponseDTO;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.Storage;
import bg.sofia.uni.fmi.mjt.battleships.online.server.storage.UserIn;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.DEFAULT;
import static bg.sofia.uni.fmi.mjt.battleships.online.server.command.manager.CommandExecutor.RED;

public class ListUsers {
    private static final String TOTAL_USERS_MSG = "Total users connected now: ";

    public static CommandResponseDTO execute(Storage storage) {
        Collection<UserIn> users = storage.getUsersInAsStream().collect(Collectors.toCollection(LinkedList::new));
        Iterator<UserIn> usersIterator = users.iterator();

        StringBuilder sb = new StringBuilder(TOTAL_USERS_MSG)
                .append(RED)
                .append(users.size())
                .append(DEFAULT)
                .append(System.lineSeparator());

        while (usersIterator.hasNext()) {
            UserIn currUser = usersIterator.next();

            sb.append(RED).append("> ").append(DEFAULT)
                    .append(currUser.getUsername())
                    .append(" is ").append(currUser.getStatus())
                    .append(System.lineSeparator());
        }

        return new CommandResponseDTO(sb.toString().trim());
    }
}
