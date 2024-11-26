
function initializePopup(mensaje) {
    if (mensaje) {
        const popup = document.getElementById('errorPopup');

        // Mostrar el popup
        popup.classList.add('show');

        // Programar para ocultar después de 3 segundos
        setTimeout(function() {
            popup.classList.add('hide');
            setTimeout(function() {
                popup.classList.remove('show');
                popup.classList.remove('hide');
            }, 500);
        }, 3000);
    }
}

// Cuando el documento está listo
document.addEventListener('DOMContentLoaded', function() {
    // Esta línea obtiene el mensaje de Thymeleaf
    const mensaje = document.getElementById('errorPopup')?.getAttribute('data-mensaje');
    initializePopup(mensaje);
});

