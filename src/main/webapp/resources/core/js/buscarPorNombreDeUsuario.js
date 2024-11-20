document.addEventListener('keyup', e => {
    if (e.target.matches('#usuarioNombre')) {
        const searchTerm = e.target.value.toLowerCase();
        document.querySelectorAll('.usuario-item').forEach(usuario => {
            const nombre = usuario.querySelector('.usuario-nombre').textContent.toLowerCase();

            if (nombre.includes(searchTerm)) {
                usuario.classList.remove('filtro');
            } else {
                usuario.classList.add('filtro');
            }
        });
    }
});






