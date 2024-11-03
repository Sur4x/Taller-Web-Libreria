function darLike(comentarioId) {
    console.log(`Función darLike llamada con comentarioId: ${comentarioId}`);
    fetch(`/club/comentarios/${comentarioId}/like`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        console.log(`Respuesta recibida: ${response.status}`);
        if (!response.ok) {
            throw new Error('Error en la respuesta del servidor');
        }
        return response.json();
    })
    .then(nuevoContadorLikes => {
        console.log(`Nuevo contador de likes: ${nuevoContadorLikes}`);
        document.getElementById(`like-count-${comentarioId}`).innerText = nuevoContadorLikes;
    })
    .catch(error => console.error("Error al dar 'Me gusta':", error));
}


