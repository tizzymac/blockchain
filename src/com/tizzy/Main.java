package com.tizzy;

import io.javalin.Javalin;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Main {

    public static void main( String[] args ) {

        int port = (args == null) ? Integer.parseInt(args[0]) : 1917;

        // Instantiate this node
        Javalin app = Javalin.create().start(port);

        // Generate a globally unique address for this node
        String node_identifier = UUID.randomUUID().toString();

        // Instantiate the blockchain
        Blockchain blockchain = new Blockchain();


        app.get("/chain", ctx -> {
            ctx.result(new JSONObject()
                        .put("chain", blockchain.getChain())
                        .put("length", blockchain.getLength())
                        .toString())
                    .status(200);
        });


        app.get("/mine", ctx -> {
            // We run the proof of work algorithm to get the next proof...
            Block last_block = blockchain.last_block();
            int last_proof = last_block.proof;
            int proof = blockchain.proof_of_work(last_proof);

            // We must receive a reward for finding the proof.
            // The sender is "0" to signify that this node has mined a new coin.
            blockchain.new_transaction("0", node_identifier, 1);

            // Forge the new Block by adding it to the chain
            String previous_hash = blockchain.hash(last_block);
            Block block = blockchain.new_block(proof, previous_hash);

            ctx.result(new JSONObject()
                        .put("message", "New Block Forged")
                        .put("index", block.index)
                        .put("transactions", block.transactions)
                        .put("proof", block.proof)
                        .put("previous_hash", block.previous_hash)
                        .toString())
                    .status(200);
        });

        app.get("/nodes/resolve", ctx -> {
            Boolean replaced = blockchain.resolve_conflicts();

            if (replaced) {
                ctx.result(new JSONObject()
                        .put("message", "Our chain was replaced")
                        .put("new_chain", blockchain.chain)
                        .toString())
                .status(200);

            } else {
                ctx.result(new JSONObject()
                        .put("message", "Our chain is authoritative")
                        .put("chain", blockchain.chain)
                        .toString())
                .status(200);
            }
        });

        app.post("/transactions/new", ctx -> {
            JSONObject values = new JSONObject(ctx.body());

            // Check that the required fields are in the POST'ed data
            boolean no_missing_values = (values.has("sender"));
            no_missing_values &= values.has("recipient");
            no_missing_values &= values.has("amount");
            if (no_missing_values) {

                // Create a new Transaction
                int index = blockchain.new_transaction(
                        values.get("sender").toString(),
                        values.get("recipient").toString(),
                        Integer.parseInt(values.get("amount").toString()));
                String response = "Transaction will be added to Block " + index;
                ctx.result(response).status(201);
            } else {

                // Missing values
                ctx.status(200);
            }
        });

        app.post("/nodes/register", ctx -> {
            JSONObject values = new JSONObject(ctx.body());

            List<URL> nodes = new ArrayList<>();
            for (Object node : values.getJSONArray("nodes")) {
                nodes.add(new URL(node.toString()));
            }
            if (nodes.isEmpty()) {
                // return "Error: Please supply a valid list of nodes",400
            }

            for (URL node : nodes) {
                blockchain.register_node(node);
            }

            ctx.result(new JSONObject()
                    .put("message", "New nodes have been added.")
                    .put("total_nodes", blockchain.nodes)
                    .toString())
                    .status(201);
        });

    }

}
