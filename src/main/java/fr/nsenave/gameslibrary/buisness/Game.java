package fr.nsenave.gameslibrary.buisness;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Game {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String console;

    @Getter @Setter
    private int release;

    @Override
    public String toString() {
        return hashCode() + ", "
                + "name: " + name + ", "
                + "console: " + console + ", "
                + "release: " + release;
    }
}
