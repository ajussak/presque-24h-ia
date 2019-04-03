package pkg24h;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GroupeCarte extends ArrayList<Carte> {

    public int foundType(TypeCarte typeCarte) {
        OptionalInt optionalInt = IntStream.range(0, this.size()).filter(i -> this.get(i).getType().equals(typeCarte)).findFirst();
        return optionalInt.isPresent() ? optionalInt.getAsInt() : -1;
    }

    public int foundValue(int value) {
        OptionalInt optionalInt = IntStream.range(0, this.size()).filter(i -> this.get(i).getValeur() > value).findFirst();
        return optionalInt.isPresent() ? optionalInt.getAsInt() : -1;
    }

    public int foundExactValue(int value) {
        OptionalInt optionalInt = IntStream.range(0, this.size()).filter(i -> this.get(i).getValeur() == value).findFirst();
        return optionalInt.isPresent() ? optionalInt.getAsInt() : -1;
    }

    public String foundValueName(int value) {
        Optional<Carte> optionalCarte = this.stream().filter(carte -> carte.getValeur() > value).findFirst();
        return optionalCarte.map(Carte::getNom).orElse(null);
    }

    public String foundSameNames() {
        List<String> list = this.stream().map(Carte::getNom).collect(Collectors.toList());

        String prev = "";
        for (String name : list) {
            if (name != null && name.equals(prev))
                return name;
            prev = name;
        }
        return null;
    }

    public long countType(TypeCarte typeCarte)
    {
        return this.stream().filter(carte -> carte.getType().equals(typeCarte)).count();
    }

}
