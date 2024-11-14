function previewImage(input) {
    const preview = document.getElementById('imagePreview');
    if (input.files && input.files[0]) {
        const reader = new FileReader();
        reader.onload = function (e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
        }
        reader.readAsDataURL(input.files[0]);
    }
}

function openPreviewModal(imageSrc) {
    const modal = document.getElementById('previewModal');
    const modalImage = document.getElementById('modalImage');
    modalImage.src = imageSrc;
    modal.style.display = 'block';
}

function closePreviewModal() {
    const modal = document.getElementById('previewModal');
    modal.style.display = 'none';
}

// 모달 외부 클릭 시 닫기
window.onclick = function (event) {
    const modal = document.getElementById('previewModal');
    if (event.target == modal) {
        modal.style.display = 'none';
    }
}

function updateRequiredStatus(imageId, isRequired) {
    $.ajax({
        url: '/admin/images/' + imageId + '/required',
        type: 'PUT',
        data: JSON.stringify({isRequired: isRequired}),
        contentType: 'application/json',
        success: function (response) {
            alert('상태가 성공적으로 업데이트되었습니다.');
        },
        error: function (xhr, status, error) {
            alert('상태 업데이트 중 오류가 발생했습니다: ' + error);
            $(event.target).prop('checked', !isRequired);
        }
    });
}

function deleteImage(button) {
    const imageId = button.getAttribute('data-id');
    if (confirm('이 이미지를 삭제하시겠습니까?')) {
        $.ajax({
            url: '/admin/images/' + imageId,
            type: 'DELETE',
            success: function (response) {
                location.reload();
            },
            error: function (xhr, status, error) {
                alert('이미지 삭제 중 오류가 발생했습니다: ' + error);
            }
        });
    }
}