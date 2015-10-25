public class Main {

    public static void main(String[] args) {

        System.out.println("Iniciando servidor P2P...");
        ServerP2p server = new ServerP2p();

        System.out.println("Servidor ejecuntadose...");
        server.run();

    }
}
