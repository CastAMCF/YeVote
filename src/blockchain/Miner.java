package blockchain;

public class Miner extends Thread {
	/*
	//limit of nonce
    public static int MAX_NONCE = (int)1E9;
    
    int nonce, dificulty;
    String data;
    AtomicInteger ticket;

    public Miner(String data, int dificulty) {
        this.ticket = new AtomicInteger(0);
        this.data = data;
        this.dificulty = dificulty;
    }

    @Override
    public void run() {
        String zeros = String.format("%0" + dificulty + "d", 0);
        //starting nonce
        while ((nonce = ticket.getAndIncrement()) < MAX_NONCE) {
            //calculate hash of block
            String hash = Hash.getHash(nonce + data);
            //Nounce found
            if (hash.startsWith(zeros)) {
                interrupt();
                return;
            }
        }
    }
    
    public static int getNonce(String data, int dificulty) throws InterruptedException {
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        // 1 - criar um array de threads com o n. de processadores do computador
        int numberOfProcessors = Runtime.getRuntime().availableProcessors();
        Miner[] threads = new Miner[numberOfProcessors];
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //2 - criar as threads

        for (int i = 0; i < threads.length; i++) {
            // 2.2 - criar as threads
            threads[i] = new Miner(data, dificulty);
            // 2.3 - executar as threads
            threads[i].start();
        }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //3 - esperar que todas as threads terminem
        int nonce = 0;
        for (int i = 0; i < threads.length; i++) {
            //esperar pela thread i 
            threads[i].join();
            //actualizar
            nonce = threads[i].nonce;
        }
        //::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
        //3 - Executar o calculo
        return nonce; //media do pi calculados pelas threads
    }
    */
	
	//maximum number of Nonce
    public static int MAX_NONCE = (int)1E9;

    public static int getNonce(String data, int dificulty) {
        //String of zeros
        String zeros = String.format("%0" + dificulty + "d", 0);
       //starting nonce
        int nonce = 0;
        while (nonce < MAX_NONCE) {
            //calculate hash of block
            String hash = Hash.getHash(nonce + data);
            //DEBUG .... DEBUG .... DEBUG .... DEBUG .... DEBUG .... DEBUG
            //System.out.println(nonce + " " + hash);
            //Nounce found
            if (hash.endsWith(zeros)) {
                return nonce;
            }
            //next nounce
            nonce++;
        }
        return nonce;
    }
    
}
