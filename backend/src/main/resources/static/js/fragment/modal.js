let currentOpenModalId = null;

function openPreviewModal(src) {
    document.getElementById('previewModal').style.display = "block";
    document.getElementById('modalImage').src = src;
    currentOpenModalId = 'previewModal';
}

function openInfluencerEditModal(element) {
    const editId = element.getAttribute("data-id");
    const editJob = element.getAttribute("data-job");
    const editName = element.getAttribute("data-name");

    document.getElementById('editId').value = editId;
    document.getElementById('editJob').value = editJob;
    document.getElementById('editName').value = editName;

    document.getElementById('editModal').style.display = "block";
    currentOpenModalId='editModal'
}

function openPlaceSearchModal(mapProvider, element=null) {
    console.log(mapProvider + " open");
    if (element) {
        window.selectedVideoId = element.getAttribute("data-video-id");
        window.selectedVideoUrl = element.getAttribute("data-video-url");
    }

    const modalId = `placeSearchModal-${mapProvider}`;
    document.getElementById(modalId).style.display = "block";
    const videoIFrame = `videoIFrame-${mapProvider}`;
    document.getElementById(videoIFrame).src = `https://www.youtube.com/embed/${window.selectedVideoUrl}`;

    currentOpenModalId = modalId;
}

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
