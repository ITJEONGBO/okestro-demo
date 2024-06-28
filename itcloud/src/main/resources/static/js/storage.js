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
    //네트워크 버튼
    $('#aside_popup_network_btn').click(function(event) {
        event.preventDefault(); // 기본 동작 방지
        var targetUrl = 'network.html'; // 이동할 URL
        window.location.href = targetUrl; // 페이지 이동

        $('#virtual_machine_chart').css('display','none');
        $('#storage_chart').css('display','block');

    });
    //설정 버튼
    $('#setting_icon').click(function(event) {
        event.preventDefault(); // 기본 동작 방지
        var targetUrl = 'setting.html'; // 이동할 URL
        window.location.href = targetUrl; // 페이지 이동

        $('#virtual_machine_chart').css('display','none');
        $('#storage_chart').css('display','block');

    });

// aside 팝업(스토리지 버튼)
$('#aside_popup_storage_btn').click(function() {
    $('#virtual_machine_chart').css('display', 'none');
    $('#storage_chart').css('display', 'block');
    $('#network_chart').css('display', 'none');
    $('#setting_chart').css('display', 'none');
    $('#setting_user_section').css('display', 'none');

    // 모든 버튼 배경색 초기화
    $('#aside_popup_dashboard_btn').css('background-color', '');
    $('#aside_popup_machine_btn').css('background-color', '');
    $('#aside_popup_network_btn').css('background-color', '');
    $('#setting_icon').css('background-color', '');

    // 클릭한 버튼 배경색 변경
    $('#aside_popup_storage_btn').css('background-color', 'rgb(218, 236, 245)'); // 노란색으로 변경

    // aside_popup 보이기/숨기기
    if ($('#aside_popup').css('display') === 'none') {
        $('#aside_popup').css('display', 'block');
        $('#aside_outer').css('width', '20%');
    } else {
        $('#aside_popup').css('display', 'none');
        $('#aside_outer').css('width', '3%');
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
$(document).ready(function() {
    // 모든 섹션을 숨기고 클릭된 섹션만 보이기
    var sections = [
        '.section_content_outer',
        '#storage_domain_outer',
        '#storage_volume_outer',
        '#storage_storage_outer',
        '#storage_logic_outer',
        '#storage_cluster_outer',
        '#storage_right_outer'
    ];

    // 클릭 이벤트 핸들러 설정
    $('#storage_section .content_header_left div').click(function() {
        var index = $(this).index();

        // 모든 섹션 숨기기
        sections.forEach(function(section, i) {
            if (i === index) {
                $(section).show();
            } else {
                $(section).hide();
            }
        });

        // 현재 활성화된 섹션의 content_header_left에서 active 클래스 제거
        $('#storage_section .content_header_left div').removeClass('active');

        // 클릭된 요소에 active 클래스 추가
        $(this).addClass('active');
    });

    // 처음 로딩 시 첫 번째 섹션을 기본으로 활성화
    $('#storage_section .content_header_left div').first().trigger('click');
});

    //스토리지 디스크(새로만들기) 팝업창
    $('#storage_disk_new_btn').click(function(){
        if($('.storage_disk_new_outer').css('display') === 'none'){
            $('.storage_disk_new_outer').css('display', 'block');
        } else {
            $('.storage_disk_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.storage_disk_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.storage_disk_new_outer').css('display', 'none');
    });

    // 스토리지 디스크(새로만들기->이미지) 팝업창
    $('#storage_img_btn').click(function(){
        $('#storage_directlun_outer').css('display', 'none');
        $('.storage_disk_new_outer .disk_new_img').css('display', 'block');
        $('#storage_managed_outer').css('display', 'none');
    });
    //스토리지 디스크(새로만들기->직접Lun) 팝업창
    $('#storage_directlun_btn').click(function(){
        $('#storage_directlun_outer').css('display', 'block');
        $('.storage_disk_new_outer .disk_new_img').css('display', 'none');
        $('#storage_managed_outer').css('display', 'none');
    });


    //스토리지 디스크(새로만들기->관리되는 블록) 팝업창
    $('#storage_managed_btn').click(function(){
        $('#storage_directlun_outer').css('display', 'none');
        $('.storage_disk_new_outer .disk_new_img').css('display', 'none');
        $('#storage_managed_outer').css('display', 'flex');
    });

    //스토리지 디스크(업로드) 팝업창
    $('#storage_disk_upload').click(function(){
        if($('.storage_disk_upload_outer').css('display') === 'none'){
            $('.storage_disk_upload_outer').css('display', 'block');
        } else {
            $('.storage_disk_upload_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.storage_disk_upload_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.storage_disk_upload_outer').css('display', 'none');
    });

    //스토리지 도메인(새로운 도메인) 팝업창
    $('#new_domain_btn').click(function(){
        if($('.storage_domain_new_outer').css('display') === 'none'){
            $('.storage_domain_new_outer').css('display', 'block');
        } else {
            $('.storage_domain_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.storage_domain_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.storage_domain_new_outer').css('display', 'none');
    });

    // 스토리지 도메인(새로운도메인 -> 사용자정의 연결매개변수 히든박스)
    $('#domain_hidden_box_btn').click(function(){
        if($('#domain_hidden_box').css('display') === 'none'){
            $('#domain_hidden_box').css('display', 'block');
        } else {
            $('#domain_hidden_box').css('display', 'none');
        }
    });
    // 스토리지 도메인(새로운도메인 -> 고급매개변수 히든박스)
    $('#domain_hidden_box_btn2').click(function(){
        if($('#domain_hidden_box2').css('display') === 'none'){
            $('#domain_hidden_box2').css('display', 'block');
        } else {
            $('#domain_hidden_box2').css('display', 'none');
        }
    });

    //스토리지 도메인(도메인 가져오기) 팝업창
    $('#get_domain_btn').click(function(){
        if($('.storage_domain_get_outer').css('display') === 'none'){
            $('.storage_domain_get_outer').css('display', 'block');
        } else {
            $('.storage_domain_get_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.storage_domain_get_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.storage_domain_get_outer').css('display', 'none');
    });
    //스토리지 도메인(도메인 관리) 팝업창
    $('#administer_domain_btn').click(function(){
        if($('.storage_domain_administer_outer').css('display') === 'none'){
            $('.storage_domain_administer_outer').css('display', 'block');
        } else {
            $('.storage_domain_administer_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.storage_domain_administer_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.storage_domain_administer_outer').css('display', 'none');
    });

    // 스토리지 볼륨(새로만들기)팝업
    $('#storage_volume_new_btn').click(function(){
        if($('.storage_volume_new_outer').css('display') === 'none'){
            $('.storage_volume_new_outer').css('display', 'block');
        } else {
            $('.storage_volume_new_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.storage_volume_new_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.storage_volume_new_outer').css('display', 'none');
    });

    // 스토리지 볼륨(스냅샷)팝업
    $('#storage_volume_snap_btn').click(function(){
        if($('.storage_volume_snap_outer').css('display') === 'none'){
            $('.storage_volume_snap_outer').css('display', 'block');
        } else {
            $('.storage_volume_snap_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.storage_volume_snap_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.storage_volume_snap_outer').css('display', 'none');
    });
    // 스토리지 볼륨(스냅샷 옵션박스)
    $('#storage_volume_option_boxbtn').click(function(){
        if($('.storage_volume_option_box').css('display') === 'none'){
            $('.storage_volume_option_box').css('display', 'block');
        } else {
            $('.storage_volume_option_box').css('display', 'none');
        }
    });
});