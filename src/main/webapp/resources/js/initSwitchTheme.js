const switchThemeIcon = document.querySelector('.switchThemeIcon');
const bodyApp = document.querySelector('body');

function initSwitchTheme(){
	console.log('troca cor');
    bodyApp.classList.toggle('dark');
}

switchThemeIcon.addEventListener('click', initSwitchTheme);