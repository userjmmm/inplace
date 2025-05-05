let currentOpenModalId = null;

function closeModal() {
    if (currentOpenModalId) {
        const modal = document.getElementById(currentOpenModalId);
        const iframe = modal.querySelector("iframe");
        if (modal) {
            modal.style.display = "none";
        }
        if (iframe) {
            iframe.src = "";
        }

        currentOpenModalId = null;
    }
}

// 모달 외부 클릭시 닫기
window.onclick = function (event) {
    if (currentOpenModalId) {
        const modal = document.getElementById(currentOpenModalId);
        if (event.target === modal) {
            closeModal();
        }
    }
};
