
$(function(){
    $('#header_right > span').click(function(){
        location.href = 'http://127.0.0.1:5500/Darkdashboard.html';
    });
    // aside 팝업
    $('#aside_popup_btn').click(function(){
        $('#aside_outer').css('width','3%');
        if($('#aside_popup').css('display') === 'none'){
            $('#aside_popup').css('display', 'block');
            $('#aside_outer').css('width','20%');
        } else {
            $('#aside_popup').css('display', 'none');
            $('#aside_outer').css('width','3%');
        }
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

     // aside_popup_content
    $('#aside_popup_first').click(function(){
        if($('#aside_popup_second').css('display') === 'none'){
            $('#aside_popup_second').css('display', 'block');
        } else {
            $('#aside_popup_second').css('display', 'none');
        }
    });
    $('#aside_popup_second').click(function(){
        if($('#aside_popup_last').css('display') === 'none'){
            $('#aside_popup_last').css('display', 'block');
        } else {
            $('#aside_popup_last').css('display', 'none');
        }
    });

    //편집팝업창
    $('#edit_btn').click(function(){ 
        if($('#edit_popup_bg').css('display') === 'none'){
            $('#edit_popup_bg').css('display', 'block');
        } else {
            $('#edit_popup_bg').css('display', 'none');
        }
    });
    $('.edit_header>button').click(function(){
        $('#edit_popup_bg').css('display', 'none');
    });
    $('.edit_footer>button:nth-child(3)').click(function(){
        $('#edit_popup_bg').css('display', 'none');
    });

    //편집팝업 aside
    $('.edit_aside > div').click(function() {
        $('.edit_aside > div i').css('display', 'none');
        $('.edit_aside > div').css({'background-color':'#FAFAFA','color': 'black','border-bottom':'none'});

        $(this).find('i').css('display', 'block');
        $(this).css({'background-color':'#EDEDED','color': '#1eb8ff','border-bottom':'1px solid blue'});
    });
    
    // nav 파란밑줄, 각 맞는 창불러오기 
    $('.content_header_left div').click(function() {
        var index = $(this).index();
        var parentId = $(this).closest('.content_header').parent().attr('id');

        $('.content_header_left div').removeClass('active');
        $(this).addClass('active');

        if (index === 0) {
            $('#content_outer').show();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').hide();
        } else if (index === 1) {
            $('#content_outer').hide();
            $('#network_outer').show();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').hide();
        } else if (index === 2) {
            $('#content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').show();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').hide();
        }else if (index === 3) {
            $('#content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').show();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').hide();
        }else if (index === 4) {
            $('#content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').show();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').hide();
        }else if (index === 5) {
            $('#content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').show();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').hide();
        }else if (index === 6) {
            $('#content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').show();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').hide();
        }else if (index === 7) {
            $('#content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').show();
            $('#power_outer').hide();
            $('#event_outer').hide();
        }else if (index === 8) {
            $('#content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').show();
            $('#event_outer').hide();
        }else if (index === 9) {
            $('#content_outer').hide();
            $('#network_outer').hide();
            $('#disk_outer').hide();
            $('#snapshot_outer').hide();
            $('#application_outer').hide();
            $('#pregroup_outer').hide();
            $('#pregroup_lable_outer').hide();
            $('#guest_info_outer').hide();
            $('#power_outer').hide();
            $('#event_outer').show();
        }

        
        $('#content_outer .content_header_left div').eq(index).addClass('active');
        $('#network_outer .content_header_left div').eq(index).addClass('active');
        $('#disk_outer .content_header_left div').eq(index).addClass('active');
        $('#snapshot_outer .content_header_left div').eq(index).addClass('active');
        $('#application_outer .content_header_left div').eq(index).addClass('active');
        $('#pregroup_outer .content_header_left div').eq(index).addClass('active');
        $('#pregroup_lable_outer .content_header_left div').eq(index).addClass('active');
        $('#guest_info_outer .content_header_left div').eq(index).addClass('active');
        $('#power_outer .content_header_left div').eq(index).addClass('active');
        $('#event_outer .content_header_left div').eq(index).addClass('active');
    });

    //네트워크 인터페이스(새로만들기) 팝업창
    $('#network_popup_new').click(function(){
        if($('.network_popup_outer').css('display') === 'none'){
            $('.network_popup_outer').css('display', 'block');
        } else {
            $('.network_popup_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.network_popup_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.network_popup_outer').css('display', 'none');
    });

    //디스크(새로만들기) 팝업창
    $('#disk_popup_new').click(function(){
        if($('.disk_popup_outer').css('display') === 'none'){
            $('.disk_popup_outer').css('display', 'block');
        } else {
            $('.disk_popup_outer').css('display', 'none');
        }
    });
    $('.network_popup_header>button').click(function(){
        $('.disk_popup_outer').css('display', 'none');
    });
    $('.edit_footer>button:last-child').click(function(){
        $('.disk_popup_outer').css('display', 'none');
    });
    
    
    //content_header_popup
    $('.content_header_popup_btn').click(function(event){
        event.stopPropagation(); // 이벤트 버블링 중지
        $('.content_header_popup').toggle();
    });
    // 바탕(body) 부분 클릭 이벤트
    $('body').click(function() {
        $('.content_header_popup').hide();
    });
    // 팝업창 내부 클릭 이벤트 (닫히지 않게 하기 위해)
    $('.content_header_popup').click(function(event) {
        event.stopPropagation(); // 이벤트 버블링 중지
    });



    //팝업창
    //일반버튼
    $('#common_btn').click(function() {
        $('#common_outer').css('display', 'block');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#console_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
        $('#preference_outer').css('display', 'none');
    });
    //시스템버튼
    $('#system_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'block');
        $('#host_outer').css('display', 'none');
        $('#console_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
        $('#preference_outer').css('display', 'none');
    });
    //콘솔버튼
    $('#console_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#console_outer').css('display', 'block');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
        $('#preference_outer').css('display', 'none');
    });
    
    //호스트버튼
    $('#host_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'block');
        $('#console_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
        $('#preference_outer').css('display', 'none');
    });
    //고가용성 버튼
    $('#ha_mode_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#console_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'block');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
        $('#preference_outer').css('display', 'none');
    });
    //리소스할당 버튼
    $('#res_alloc_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#console_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'block');
        $('#boot_outer').css('display', 'none');
        $('#preference_outer').css('display', 'none');
    });
    //부트 옵션버튼
    $('#boot_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#console_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'block');
        $('#preference_outer').css('display', 'none');
    });
    //선호도 버튼
    $('#preference_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#console_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
        $('#preference_outer').css('display', 'block');
    });

   // footer 팝업창
    $('#footer>button').click(function(){
        if($('#footer_content').css('display') === 'none'){
            $('#footer_content').css('display', 'block');
            $('')
        } else {
            $('#footer_content').css('display', 'none');
        }
    });

    //footer 최근작업,경보 선택
    $("#footer>div>a").click(function(){
        // 클릭한 div의 현재 글자색가져오기
        var currentColor = $(this).css("color");
        // 만약 현재 글자색이 회색이라면
        if (currentColor === "black") {
            // 계속 검정색유지
            $(this).css("color", "black");
        } else { // 만약 회색이아니라면
            $("#footer>div>a").css('color','#4F4F4F');
            $("#footer>div>a").css('border-bottom','none');
            $(this).css("color", "black");
            $(this).css("border-bottom", "1px solid blue");
        }
    });
});
