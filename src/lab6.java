import java.util.Scanner;

public class lab6 {
    static int totalWins = 0;
    static int totalLosses = 0;

    // Build a full deck of 52 cards in order
    public static LinkedList initialize_deck() {
        LinkedList deck = new LinkedList();
        for (Card.suites s : Card.suites.values()) {
            for (Card.ranks r : Card.ranks.values()) {
                if (r != Card.ranks.NULL && s != Card.suites.NULL) {
                    Card newCard = new Card(s, r);
                    deck.add_at_tail(newCard);
                }
            }
        }
        return deck;
    }

    // If player loses 3 rounds in a row, this method resets the game:
    // it creates a new full deck, shuffles it, and deals new hands.
    public static void rage_quit(LinkedList player1, LinkedList computer, LinkedList deck) {
        System.out.println("\nRage Quit Triggered! Resetting the game...\n");
        LinkedList newDeck = initialize_deck();
        newDeck.shuffle(512);

        player1.clear();
        computer.clear();
        for (int i = 0; i < 5; i++) {
            player1.add_at_tail(newDeck.remove_from_head());
            computer.add_at_tail(newDeck.remove_from_head());
        }
        deck.clear();
        while (newDeck.size > 0) {
            deck.add_at_tail(newDeck.remove_from_head());
        }
    }

    // Compare two cards.
    // Returns a positive number if player's card is higher, negative if lower.
    // If ranks are equal, uses suit value as tie-breaker.
    private static int compareCards(Card player, Card comp) {
        int rankDiff = player.getRankValue() - comp.getRankValue();
        if (rankDiff != 0) return rankDiff;
        return player.getSuitValue() - comp.getSuitValue();
    }

    // Play the Blind Man's Bluff game:
    // The player sees the computer's card and guesses if their own card is higher or lower.
    // If the player loses three rounds in a row, rage_quit is invoked.
    private static void play_blind_mans_bluff(LinkedList player1, LinkedList computer, LinkedList deck) {
        Scanner scanner = new Scanner(System.in);
        int consecutiveLosses = 0;
        int rounds = 5; // 5 rounds per hand

        for (int i = 0; i < rounds; i++) {
            System.out.println("\nRound " + (i + 1));
            if (player1.size == 0 || computer.size == 0) {
                System.out.println("Not enough cards to play.");
                break;
            }

            Card compCard = computer.remove_from_head();
            System.out.print("Computer's card is: ");
            compCard.print_card();
            System.out.println();

            System.out.print("Is your card higher or lower than the computer's card? (h/l): ");
            String guess = scanner.nextLine();

            Card playerCard = player1.remove_from_head();

            System.out.print("Your card is: ");
            playerCard.print_card();
            System.out.println();

            int result = compareCards(playerCard, compCard);
            boolean playerWins = false;
            if (result > 0 && guess.equalsIgnoreCase("h")) {
                playerWins = true;
            } else if (result < 0 && guess.equalsIgnoreCase("l")) {
                playerWins = true;
            }

            if (playerWins) {
                System.out.println("You win this round!");
                totalWins++;
                consecutiveLosses = 0;
            } else {
                System.out.println("You lose this round!");
                totalLosses++;
                consecutiveLosses++;
            }

            if (consecutiveLosses == 3) {
                rage_quit(player1, computer, deck);
                consecutiveLosses = 0;
                System.out.println("Game has been reset due to 3 consecutive losses. Starting new hand.");
                i = -1;
            }
        }

        System.out.println("\nGame Over!");
        System.out.println("Total Wins: " + totalWins);
        System.out.println("Total Losses: " + totalLosses);
        scanner.close();
    }

    public static void main(String[] args) {
        LinkedList deck = initialize_deck();
        System.out.println("Initial deck:");
        deck.print();
        deck.sanity_check();

        deck.shuffle(512);
        System.out.println("Shuffled deck:");
        deck.print();
        deck.sanity_check();

        LinkedList player1 = new LinkedList();
        LinkedList computer = new LinkedList();
        int num_cards_dealt = 5;
        for (int i = 0; i < num_cards_dealt; i++) {
            player1.add_at_tail(deck.remove_from_head());
            computer.add_at_tail(deck.remove_from_head());
        }

        play_blind_mans_bluff(player1, computer, deck);
    }
}