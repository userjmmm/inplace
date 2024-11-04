function openModal() {
    const modal = document.getElementById("placeSearchModal");
    modal.style.display = "block"; // 모달을 표시
}

function closeModal() {
    const modal = document.getElementById("placeSearchModal");
    modal.style.display = "none"; // 모달을 숨김
}

// 모달 외부를 클릭했을 때 모달을 닫기 위한 이벤트 리스너
window.onclick = function (event) {
    const modal = document.getElementById("placeSearchModal");
    if (event.target === modal) {
        modal.style.display = "none";
    }
};

var currentPage = 0;
var pageSize = 10;

function getNullVideos(page) {
    $.ajax({
        url: '/videos/null?page=' + page + '&size=' + pageSize,
        method: 'GET',
        success: function (data) {
            var contents = data.content;
            var tbody = $('#video-tbody');
            tbody.empty(); // 초기화

            // 데이터에 따라 테이블 행을 생성
            contents.forEach(function (video) {
                imageUrl = "https://img.youtube.com/vi/" + getYouTubeVideoId(video.videoUrl) + "/0.jpg"
                // 한 행의 내용
                var row = `
                        <tr>
                            <td>${video.videoId}</td>
                            <td><img src="${imageUrl}" alt="유튜브썸네일"></td>
                            <td>
                                <button class="toggle-btn" onclick="openModal()">등록</button>
                            </td>
                        </tr>
                    `;
                tbody.append(row);

                currentPage = data.number;

                $('#prevPage').prop('disabled', data.first);
                $('#nextPage').prop('disabled', data.last);
            });
        },
        error: function (err) {
            if (err.responseJSON) {
                var s = JSON.stringify(err.responseJSON);
                alert(s);
            } else {
                alert("문제가 발생했습니다! 상태 코드 : " + err.status)
            }
        }
    });
}

$(document).ready(function () {
    getNullVideos(currentPage);

    $('#prevPage').click(function () {
        if (currentPage > 0) {
            getNullVideos(currentPage - 1);
        }
    });

    $('#nextPage').click(function () {
        getNullVideos(currentPage + 1);
    });
});

function getYouTubeVideoId(url) {
    const urlObj = new URL(url);  // Create a URL object from the input URL
    return urlObj.searchParams.get('v');  // Extract the 'v' parameter from the query string
}

// 장소 검색 객체 생성
var ps = new kakao.maps.services.Places();

// 키워드 검색을 요청하는 함수
function searchPlaces() {
    var keyword = document.getElementById('keyword').value;

    if (!keyword.trim()) {
        alert('키워드를 입력해주세요!');
        return;
    }

    // 키워드로 장소 검색
    ps.keywordSearch(keyword, function (data, status) {
        if (status === kakao.maps.services.Status.OK) {
            var tbody = $('#search-tbody');
            tbody.empty()
            for (let i = 0; i < data.length; i++) {
                var row = `
                        <tr>
                            <td>${data[i].place_name}</td>
                            <td>${data[i].address_name}</td>
                            <td>${data[i].id}</td>
                            <td><button class="regi-btn" onclick="placeSetting(${data[i].id})">등록</button></td>
                        </tr>
                    `;
                tbody.append(row);
                const placeId = data[i].id;  // 각 장소의 고유 id
                console.log(`장소명: ${data[i].place_name}, 주소: ${data[i].address_name} ID: ${placeId}`);
            }
        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
            alert('검색 결과가 존재하지 않습니다.');
        } else if (status === kakao.maps.services.Status.ERROR) {
            alert('검색 결과 중 오류가 발생했습니다.');
        }
    });
}

function placeSetting(id) {
    console.log(id);
}