const btnCopyText = document.querySelector('#btnCopyEmail');
const toast = document.querySelector('.toast');
const closeIcons = document.querySelector('.close');
const progress = document.querySelector('.progress');


function copyEmail(){
    const textEmail = btnCopyText.innerText;
    navigator.clipboard.writeText(textEmail);
	toast.classList.add("activeToast");
	progress.classList.add("activeProgress");
	
	setTimeout(() => {
		toast.classList.remove("activeToast");
	}, 5000);
	
	setTimeout(() => {
		progress.classList.remove("activeProgress");
	}, 5300);
}

function closeToastNotification(){
	toast.classList.remove("activeToast");
	
	setTimeout(() => {
		progress.classList.remove("activeProgress");
	}, 300);
}

btnCopyText.addEventListener('click', copyEmail);
closeIcons.addEventListener('click', closeToastNotification);