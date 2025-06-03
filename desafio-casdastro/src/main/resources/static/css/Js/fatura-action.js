document.addEventListener('DOMContentLoaded', function() {
    const mesAnoReferenciaInput = document.getElementById('mesAnoReferencia');
    if (mesAnoReferenciaInput) {
        mesAnoReferenciaInput.addEventListener('input', function (e) {
            let value = e.target.value.replace(/\D/g, ''); 
            if (value.length > 4) {
                value = value.substring(0, 6); 
            }
            if (value.length > 2) {
                value = value.replace(/(\d{2})(\d+)/, '$1/$2');
            }
            e.target.value = value;
        });
    }
});