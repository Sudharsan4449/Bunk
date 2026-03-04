const localtunnel = require('localtunnel');
const fs = require('fs');

(async () => {
    try {
        const tunnel = await localtunnel({ port: 3000 });
        console.log(`Tunnel URL: ${tunnel.url}`);
        fs.writeFileSync('tunnel_url.txt', tunnel.url);

        tunnel.on('close', () => {
            console.log('Tunnel closed');
        });
    } catch (err) {
        console.error('Tunnel error:', err);
    }
})();
