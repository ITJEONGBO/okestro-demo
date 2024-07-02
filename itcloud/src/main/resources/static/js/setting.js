$(function(){
    //대시보드 버튼
    $('#aside_popup_dashboard_btn').click(function(event) {
        event.preventDefault(); // 기본 동작 방지
        var targetUrl = 'Darkdashboard5.html'; // 이동할 URL
        window.location.href = targetUrl; // 페이지 이동

        $('#virtual_machine_chart').css('display','block');

    });
    //가상머신 버튼
    $('#aside_popup_machine_btn').click(function(event) {
        event.preventDefault(); // 기본 동작 방지
        var targetUrl = 'machine.html'; // 이동할 URL
        window.location.href = targetUrl; // 페이지 이동

    });
    //스토리지 버튼
    $('#aside_popup_storage_btn').click(function(event) {
        event.preventDefault(); // 기본 동작 방지
        var targetUrl = 'storage.html'; // 이동할 URL
        window.location.href = targetUrl; // 페이지 이동

        $('#virtual_machine_chart').css('display','none');
        $('#storage_chart').css('display','block');

    });
    //네트워크 버튼
    $('#aside_popup_network_btn').click(function(event) {
        event.preventDefault(); // 기본 동작 방지
        var targetUrl = 'network.html'; // 이동할 URL
        window.location.href = targetUrl; // 페이지 이동

        $('#virtual_machine_chart').css('display','none');
        $('#storage_chart').css('display','block');

    });
    // aside 팝업(세팅 버튼 수정필요)
    $('#setting_icon').click(function(){
        $('setting_user_section').css('display','none');

        $('#virtual_machine_chart').css('display','none');
        $('#storage_chart').css('display','none');
        $('#network_chart').css('display','none');
        $('#setting_chart').css('display','block');
        if ($('#aside_popup').css('display') === 'none' || $('#setting_icon').css('background-color') !== 'rgb(218, 236, 245)') {
            $('#aside_popup').css('display', 'block');
            $('#aside_outer').css('width','20%');
            $('#setting_icon').css('background-color', 'rgb(218, 236, 245)'); // 배경색을 노란색으로 변경
            $('#aside_popup_dashboard_btn').css('background-color', '');
            $('#aside_popup_machine_btn').css('background-color', ''); // 다른 버튼 초기화
            $('#aside_popup_network_btn').css('background-color', ''); // 다른 버튼 초기화
            $('#aside_popup_storage_btn').css('background-color', ''); // 다른 버튼 초기화
            

            } else {
                $('#aside_popup').css('display', 'none');
                $('#aside_outer').css('width','3%');

            }
    });
//---------------------------------공통----------------------------------------------------------------------------------------------------------------------------
  // aside차트(가상머신)
  $('#aside_popup_first').click(function(){
    $('#aside_popup_second').toggle();
    $('#aside_popup_last').css('display','none');
});
$('#aside_popup_second').click(function(){
    $('#aside_popup_last').toggle();
});
// aside차트(스토리지)
$('#aside_popup_first2').click(function(){
    $('#aside_popup_second2').toggle();
    $('#aside_popup_last2').css('display','none');
});
$('#aside_popup_second2').click(function(){
    $('#aside_popup_last2').toggle();
});
// aside차트(네트워크)
$('#aside_popup_first3').click(function(){
    $('#aside_popup_second3').toggle();
});
    
// aside메뉴들 색
$("#nav>div").click(function(){
    // 클릭한 div의 현재 글자색가져오기
    var currentColor = $(this).css("color");
    // 만약 현재 글자색이 검정이라면
    if (currentColor === "black") {
        // 계속 검정색유지
        $(this).css("color", "black");
    } else { // 만약 검정이아니라면
        $("#nav>div").css('color','rgb(143, 143, 143)');
        $(this).css("color", "black");
    }
});

$('#popup_btn').click(function(){
    if($('#popup_box').css('display') === 'none'){
        $('#popup_box').css('display', 'block');
    } else {
        $('#popup_box').css('display', 'none');
    }
});

var previousDiv = null;

// 우클릭 메뉴박스
$('#aside_popup_last div').contextmenu(function(event){
    // 만약 이전에 클릭한 div가 있으면 배경색 초기화
    if (previousDiv !== null) {
        previousDiv.css('background-color', '');
    }

    var currentColor = $(this).css("background-color");

    // 현재 div의 배경색을 변경하고, previousDiv에 현재 div 저장
    if (currentColor === "none" || currentColor === "rgba(0, 0, 0, 0)") {
        $(this).css("background-color", "none");
    } else {
        $(this).css('background-color', '#e6eefa');
    }

    previousDiv = $(this);
    
    event.preventDefault();
    $('#context_menu').css({
        display: 'block',
        left: event.pageX,
        top: event.pageY
    });
});

// 문서의 다른 곳을 클릭하면 배경색 초기화
$('#context_menu').hide();
if (previousDiv !== null) {
    previousDiv.css('background-color', '');
    previousDiv = null; // 초기화 후 previousDiv 리셋
}
$('#context_menu').click(function(event){
    event.stopPropagation();
});


// 공통팝업창 점3개
// 가져오기 팝업창
$('.get_btn').click(function(){
if($('#get_popup_bg').css('display') === 'none'){
    $('#get_popup_bg').css('display', 'block');
} else {
    $('#get_popup_bg').css('display', 'none');
}
});
$('.domain_header>button').click(function(){
    $('#get_popup_bg').css('display', 'none');
});
$('#get_footer_btn').click(function(){
    $('#get_popup_bg').css('display', 'none');
});
//템플릿 생성 팝업창
$('#template_btn').click(function(){
    if($('#template_bg').css('display') === 'none'){
        $('#template_bg').css('display', 'block');
    } else {
        $('#template_bg').css('display', 'none');
    }
});
$('.domain_header>button').click(function(){
    $('#template_bg').css('display', 'none');
});
$('#template_footer_btn').click(function(){
    $('#template_bg').css('display', 'none');
});

// 도메인팝업창1
$('#domain').click(function(){
    if($('#popup_bg').css('display') === 'none'){
        $('#popup_bg').css('display', 'block');
    } else {
        $('#popup_bg').css('display', 'none');
    }
});
$('.domain_header>button').click(function(){
    $('#popup_bg').css('display', 'none');
});
$('#domain_footer>a:last-child').click(function(){
    $('#popup_bg').css('display', 'none');
});

// 도메인팝업창2
$('#domain2').click(function(){
    if($('#popup_bg2').css('display') === 'none'){
        $('#popup_bg2').css('display', 'block');
    } else {
        $('#popup_bg2').css('display', 'none');
    }
});
$('.domain_header>button').click(function(){
    $('#popup_bg2').css('display', 'none');
});
$('#domain2_footer>button').click(function(){
    $('#popup_bg2').css('display', 'none');
});


//  마이그레이션 팝업창
$('#migration_btn').click(function(){
    if($('#migration_popup_outer').css('display') === 'none'){
        $('#migration_popup_outer').css('display', 'block');
    } else {
        $('#migration_popup_outer').css('display', 'none');
    }
});
$('.domain_header>button').click(function(){
    $('#migration_popup_outer').css('display', 'none');
});
$('#migration_footer>a:last-child').click(function(){
    $('#migration_popup_outer').css('display', 'none');
});

//  ova 팝업창
$('#ova_btn').click(function(){
    if($('#ova_outer').css('display') === 'none'){
        $('#ova_outer').css('display', 'block');
    } else {
        $('#ova_outer').css('display', 'none');
    }
});
$('.domain_header>button').click(function(){
    $('#ova_outer').css('display', 'none');
});
$('#ova_footer>button:last-child').click(function(){
    $('#ova_outer').css('display', 'none');
});


// footer 팝업창
$('.footer>button').click(function(){
    if($('.footer_content').css('display') === 'none'){
        $('.footer_content').css('display', 'block');
    } else {
        $('.footer_content').css('display', 'none');
    }
});

//footer 최근작업,경보 선택
$(".footer>div>a").click(function(){
    // 클릭한 div의 현재 글자색가져오기
    var currentColor = $(this).css("color");
    // 만약 현재 글자색이 회색이라면
    if (currentColor === "black") {
        // 계속 검정색유지
        $(this).css("color", "black");
    } else { // 만약 회색이아니라면
        $(".footer>div>a").css('color','#4F4F4F');
        $(".footer>div>a").css('border-bottom','none');
        $(this).css("color", "black");
        $(this).css("border-bottom", "1px solid blue");
    }
});
//---------------------------------------------------------------------------------------------------------------------------------------
    //  설정(설정) 팝업
    $('#setting_miniset_btn').click(function(){
        if($('.setting_setting_outer').css('display') === 'none'){
            $('.setting_setting_outer').css('display', 'block');
            
        } else {
            $('.setting_setting_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.setting_setting_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.setting_setting_outer').css('display', 'none');
    });


    //  설정(설정) 팝업(역할,시스템권한,스케줄링정책 등)
    showForm('part');

    // 폼을 표시하는 함수
    function showForm(type) {
        const forms = ['part', 'system', 'schedule', 'instant', 'mac'];
        forms.forEach(function(form) {
            if (form === type) {
                $(`#setting_${form}_form`).show();
                $(`#setting_${form}_btn`).addClass('active').css('color', 'blue');
            } else {
                $(`#setting_${form}_form`).hide();
                $(`#setting_${form}_btn`).removeClass('active').css('color', '');
            }
        });
    }

    // 설정 버튼 클릭 핸들러
    ['part', 'system', 'schedule', 'instant', 'mac'].forEach(function(type) {
        $(`#setting_${type}_btn`).click(function() {
            showForm(type);
        });
    });

   
    // 세팅 목록별 section
    // 활성 사용자 세션
    $('#setting_normal_btn').click(function(){
        $('#setting_user_section').css('display','none');
        $('#setting_section').css('display','block');
        
    });
    // 사용자버튼
    $('#setting_user_btn').click(function(){
        $('#setting_user_section').css('display','block');
        $('#setting_section').css('display','none');
    });

    // 팝업 닫기 버튼 클릭 핸들러
    $('.network_popup_header>button').click(function(){
        $('.setting_setting_outer').css('display', 'none');
    });

    // 팝업 닫기 (취소 버튼 클릭 핸들러)
    $('.edit_footer>button:contains("취소")').click(function(){
        $('.setting_setting_outer').css('display', 'none');
    });

    // 역할 새로만들기
    $('#setting_part_new_btn').click(function(){
        if($('.setting_part_new_outer').css('display') === 'none'){
            $('.setting_part_new_outer').css('display', 'block');
            
        } else {
            $('.setting_part_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.setting_part_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.setting_part_new_outer').css('display', 'none');
    });

    // 시스템권한 추가
    $('#setting_system_add_btn').click(function(){
        if($('.setting_system_new_outer').css('display') === 'none'){
            $('.setting_system_new_outer').css('display', 'block');
            
        } else {
            $('.setting_system_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.setting_system_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.setting_system_new_outer').css('display', 'none');
    });

    // 스케줄링 정책->새로만들기 팝업
    $('#setting_schedule_new_btn').click(function(){
        if($('.setting_schedule_new_outer').css('display') === 'none'){
            $('.setting_schedule_new_outer').css('display', 'block');
            
        } else {
            $('.setting_schedule_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.setting_schedule_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.setting_schedule_new_outer').css('display', 'none');
    });

    // MAC주소 풀->새로만들기 팝업
    $('#setting_mac_new_btn').click(function(){
        if($('.setting_mac_new_outer').css('display') === 'none'){
            $('.setting_mac_new_outer').css('display', 'block');
            
        } else {
            $('.setting_mac_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.setting_mac_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.setting_mac_new_outer').css('display', 'none');
    });

    // MAC주소 풀->편집 팝업
    $('#setting_mac_edit_btn').click(function(){
        if($('.setting_mac_edit_outer').css('display') === 'none'){
            $('.setting_mac_edit_outer').css('display', 'block');
            
        } else {
            $('.setting_mac_edit_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.setting_mac_edit_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.setting_mac_edit_outer').css('display', 'none');
    });

    // 설정->사용자->추가
    $('#set_user_add_btn').click(function(){
        if($('.setting_user_new_outer').css('display') === 'none'){
            $('.setting_user_new_outer').css('display', 'block');
            
        } else {
            $('.setting_user_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.setting_user_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.setting_user_new_outer').css('display', 'none');
    });

});
