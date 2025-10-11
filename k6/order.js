import http from 'k6/http';
import {check, sleep} from 'k6';

export let options = {
    vus: 1,              // un seul thread (il exécute en boucle les échanges)
    duration: '300s',    // 5 minutes
};

// Définition fixe des utilisateurs
const testUser = {
    email: "test.test@hotmail.com",
    auth: "Basic dGVzdC50ZXN0QGhvdG1haWwuY29tOnRlc3Q=",
    symbols: ["AAPL", "GOOG"]
};

const bobUser = {
    email: "bob@example.com",
    auth: "Basic Ym9iQGV4YW1wbGUuY29tOnRlc3Q=",
    symbols: ["AAPL", "TSLA"]
};

// Petite fonction utilitaire pour créer un ordre
function createOrder(user, side, symbol, price, quantity, type = "LIMIT", timeInForce = "GTC") {
    return {
        symbol,
        side,
        type,
        quantity,
        price,
        timeInForce,
        emailUser: user.email
    };
}

export default function () {
    const url = "http://brockerx:8080/api/order";

    let cycle = 0;

    while (true) {
        const symbol = "AAPL"; // alterne symboles pour variation
        const price = 150;
        const quantity = 5;

        // Séquence des échanges
        const trades = [
            {
                buyer: testUser,
                seller: bobUser,
                sideBuyer: "BUY",
                sideSeller: "SELL"
            },
            {
                buyer: bobUser,
                seller: testUser,
                sideBuyer: "BUY",
                sideSeller: "SELL"
            },
        ];

        for (const trade of trades) {
            // --- Le vendeur poste un SELL ---
            const sellOrder = createOrder(trade.seller, "SELL", symbol, price, quantity);
            const sellRes = http.post(url, JSON.stringify(sellOrder), {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': trade.seller.auth,
                    'Accept': 'application/json',
                    'Idempotency-Key': `${Math.random()}`
                }
            });

            // --- L’acheteur poste un BUY ---
            const buyOrder = createOrder(trade.buyer, "BUY", symbol, price, quantity);
            const buyRes = http.post(url, JSON.stringify(buyOrder), {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': trade.buyer.auth,
                    'Accept': 'application/json',
                    'Idempotency-Key': `${Math.random()}`
                }
            });

            check(sellRes, {'SELL ok': (r) => r.status === 200 || r.status === 400});
            check(buyRes, {'BUY ok': (r) => r.status === 200 || r.status === 400});

            console.log(`
========= TRADE CYCLE =========
Symbol: ${symbol} | Price: ${price} | Quantity: ${quantity}

SELLER: ${trade.seller.email}
  -> Status: ${sellRes.status}
  -> Response: ${sellRes.body}

BUYER: ${trade.buyer.email}
  -> Status: ${buyRes.status}
  -> Response: ${buyRes.body}

================================
      `);
        }

        cycle++;
    }
}
