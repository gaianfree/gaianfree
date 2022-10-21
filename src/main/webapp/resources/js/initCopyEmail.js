const btnCopyText = document.querySelector('#btnCopyEmail');
function copyEmail(){
    const textEmail = btnCopyText.innerText;
    navigator.clipboard.writeText(textEmail)
    alert("Endereço de e-mail copiado para a area de transferencia");
}
btnCopyText.addEventListener('click', copyEmail)