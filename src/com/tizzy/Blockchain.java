package com.tizzy;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

public class Blockchain {
    /* Blockchain class is responsible for managing the chain
        - stores transactions
        - helper methods for adding blocks */

    Set<URL> nodes;
    LinkedList<Block> chain;
    List<JSONObject> current_transactions;

    public Blockchain() {

        nodes = new HashSet<>();
        chain = new LinkedList<>();
        current_transactions = new ArrayList<>();

        // create genesis block
        Block genesis = new_block(1, "100");
        chain.addFirst(genesis);
    }

    public void register_node(URL address) {
        nodes.add(address);
    }

    public Boolean valid_chain(LinkedList<Block> blockchain) {
        Block last_block = blockchain.get(0);
        int current_index = 1;

        while (current_index < blockchain.size()) {
            Block block = blockchain.get(current_index);
            System.out.println(last_block);
            System.out.println(block);
            System.out.println("\n-----------\n");

            // Check that the hash of the block is correct
            if (block.getPrevious_hash() != hash(last_block())) {
                return false;
            }

            // Check that the Proof of Work is correct
            if (!valid_proof(last_block.proof, block.proof)) {
                return false;
            }

            last_block = block;
            current_index++;
        }

        return true;
    }

    public Boolean resolve_conflicts() {
        // Consensus algorithm

//        Set<URL> neighbours = this.nodes;
//        LinkedList<Block> new_chain;
//
//        // We're only looking for chains longer than ours
//        int max_length = this.chain.size();
//
//        // Grab and verify the chains from all the nodes in our network
//        for (URL node : neighbours) {
//            response = requests.get(f 'http://{node}/chain');
//            // TODO create class for handling HTTP stuff
//        }
//
//        if (response.status_code == 200) {
//            int length = response.json()['length'];
//            LinkedList<Block> chain = response.json()['chain'];
//
//            // Check if the length is longer and the chain is valid
//            if (length > max_length && valid_chain(chain)) {
//                max_length = length;
//                new_chain = chain;
//            }
//        }
//
//        // Replace our chain if we discovered a new, valid chain longer than ours
//        if (!new_chain.isEmpty()) {
//            this.chain = new_chain;
//            return true;
//        }

        return false;
    }

    public Block new_block(int proof, String previous_hash) { // previous_hash is optional
        // Create a block in the blockchain
        Block block = new Block(chain.size() + 1, new Timestamp(System.currentTimeMillis()), current_transactions, proof, previous_hash);

        // Reset the current list of transactions
        current_transactions.clear();

        chain.add(block);
        return block;
    }

    public int new_transaction(String sender, String receiver, int amount) {

        this.current_transactions.add(new JSONObject()
                .put("sender", sender)
                .put("recipient", receiver)
                .put("amount", amount));

        return last_block().index + 1;
    }

    public int proof_of_work(int last_proof) {
        /* Simple Proof of Work Algorithm:
                - Find a number p' such that hash(pp') contains leading 4 zeroes, where p is the previous p'
                - p is the previous proof, and p' is the new proof */

        int proof = 0;
        while (!valid_proof(last_proof, proof)) {
            proof++;
        }

        return proof;
    }

    public Block last_block() {
        return this.chain.getLast();
    }

    public Boolean valid_proof(int last_proof, int proof) {
        // Does hash(last_proof, proof) contain 4 leading zeroes?
        // To adjust the difficulty, modify the number of leading zeros

        String guess = last_proof + String.valueOf(proof);
        String guess_hash = DigestUtils.sha256Hex(guess);

        return (guess_hash.substring(0, 4).equals("0000"));
    }

    public String hash(Block block) {

        return DigestUtils.sha256Hex(block.transactions.toString());
    }

    public int getLength() {
        return chain.size();
    }

    public JSONObject getChain() {
        // Each block is a json object
        JSONObject chainJSON = new JSONObject();

        for (Block b : chain) {
            chainJSON.accumulate("chain", b.getJSON());
        }

        return chainJSON;
    }
}
