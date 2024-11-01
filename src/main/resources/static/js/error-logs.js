function openModal(button) {
    const stackTrace = button.getAttribute("data-stacktrace");
    const modal = document.getElementById("stackTraceModal");
    const stackTraceContent = document.getElementById("stackTraceContent");

    stackTraceContent.textContent = stackTrace; // 스택 트레이스 내용을 설정
    modal.style.display = "block"; // 모달을 표시
}

function closeModal() {
    const modal = document.getElementById("stackTraceModal");
    modal.style.display = "none"; // 모달을 숨김
}

// 모달 외부를 클릭했을 때 모달을 닫기 위한 이벤트 리스너
window.onclick = function (event) {
    const modal = document.getElementById("stackTraceModal");
    if (event.target === modal) {
        modal.style.display = "none";
    }
};