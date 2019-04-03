package pkg24h;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main {

    private static Reseau reseau;
    private static GroupeCarte main;

    public static void main(String[] args) {

        try {
            reseau = new Reseau("127.0.0.1", 1234);
            reseau.envoyer("INSCRIRE");
            reseau.recevoir();

            String message;
            main = new GroupeCarte();
            while (!(message = reseau.recevoir()).equals("FIN")) {
                if (message.contains("DEBUT_TOUR")) {
                    main = Carte.parseCartes(message);
                    //PHASE 1
                    pioche();

                    //PHASE 2
                    if (main.foundType(TypeCarte.BOUTEILLE) != -1) {
                        String doublon = main.foundSameNames();
                        String cepage = main.foundValueName(4);
                        if (doublon != null) {
                            reseau.envoyer("POSER|" + doublon);
                            main = Carte.parseCartes(reseau.recevoir());
                        } else if (cepage != null) {
                            reseau.envoyer("POSER|" + cepage);
                            main = Carte.parseCartes(reseau.recevoir());
                        } else
                            pioche();
                    } else if (main.foundType(TypeCarte.SABOTAGE) != -1) {
                        if (Math.random() <= 1/5) {
                            Random random = new Random();
                            reseau.envoyer("SABOTER|" + (random.nextBoolean() ? 1 : -1));
                            main = Carte.parseCartes(reseau.recevoir());
                        } else
                            pioche();
                    } else
                        pioche();

                    //PHASE 3

                    List<Integer> defausse = new ArrayList<>();

                    int d = 0;

                    switch (main.size()) {
                        case 14:
                            d = 1;
                            break;
                        case 15:
                            d = 2;
                            break;
                    }

                    StringBuilder command = new StringBuilder("DEFAUSSER");

                    for (int i = 0; i < d; i++) {
                        int index;
                        if ((index = main.foundType(TypeCarte.BOUTEILLE)) != -1 && (!defausse.contains(index) || main.countType(TypeCarte.BOUTEILLE) > 1))
                            defausse.add(index);
                        else if ((index = main.foundType(TypeCarte.SABOTAGE)) != -1 && !defausse.contains(index))
                            defausse.add(index);
                        else if ((index = main.foundExactValue(1)) != -1 && !defausse.contains(index))
                            defausse.add(index);
                        else if ((index = main.foundExactValue(2)) != -1 && !defausse.contains(index))
                            defausse.add(index);
                        else if ((index = main.foundExactValue(3)) != -1 && !defausse.contains(index))
                            defausse.add(index);
                        else {
                            Random random = new Random();
                            while (!defausse.contains(i = random.nextInt())) {
                                defausse.add(i);
                            }
                        }
                    }

                    for (Integer g : defausse) {
                        command.append("|").append(g);
                    }

                    reseau.envoyer(command.toString());
                    main = Carte.parseCartes(reseau.recevoir());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void pioche() throws IOException, ErreurServeur {
        reseau.envoyer("SOMMET");
        GroupeCarte pioche = Carte.parseCartes(reseau.recevoir());
        if (main.foundType(TypeCarte.BOUTEILLE) != -1) {
            int index;
            if ((index = pioche.foundValue(3)) != -1 || (index = pioche.foundType(TypeCarte.SABOTAGE)) != -1)
                reseau.envoyer("PIOCHER|" + index);
            else {
                Random r = new Random();
                index = r.nextInt(3);
                reseau.envoyer("PIOCHER|" + index);
            }

        } else {
            int index;
            if ((index = pioche.foundType(TypeCarte.BOUTEILLE)) != -1)
                reseau.envoyer("PIOCHER|" + index);
            else {
                Random random = new Random();
                reseau.envoyer("PIOCHER|" + random.nextInt(3));
            }
        }
        main.addAll(Carte.parseCartes(reseau.recevoir()));
    }

}

