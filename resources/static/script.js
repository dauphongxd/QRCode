document.getElementById('qrForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const contents = document.getElementById('contents').value;
    const size = document.getElementById('size').value;
    const correction = document.getElementById('correction').value;
    const type = document.getElementById('type').value;

    const url = `/api/qrcode?contents=${encodeURIComponent(contents)}&size=${size}&correction=${correction}&type=${type}`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.blob();
        })
        .then(blob => {
            const img = document.getElementById('qrCodeImage');
            img.src = URL.createObjectURL(blob);
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
});