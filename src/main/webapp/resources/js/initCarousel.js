const delayToChangeImgCarousel = 5000; //ms

const slides = document.querySelector('.slides');
const slidesCount = slides.childElementCount;
const maxLeft = (slidesCount - 1) * 100 * -1;

let current = 0;

function changeSlide(next = true) {
  next
    ? (current += current > maxLeft ? -100 : current * -1)
    : (current = current < 0 ? current + 100 : maxLeft);
  slides.style.left = current + '%';
}

let autoChange = setInterval(changeSlide, delayToChangeImgCarousel);

const restart = function () {
  clearInterval(autoChange);
  autoChange = setInterval(changeSlide, delayToChangeImgCarousel);
};
