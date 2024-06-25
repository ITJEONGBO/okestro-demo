$(function() {    

    $('#header_right > span').click(function(){
        location.href = 'http://127.0.0.1:5500/Darkdashboard.html';
    });
    $('#out_btn').click(function(){
        if($('#out_btn_popup').css('display') === 'none'){
            $('#out_btn_popup').css('display', 'block');
        } else {
            $('#out_btn_popup').css('display', 'none');
        }
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
    $('#edit_footer>button:nth-child(3)').click(function(){
        $('#edit_popup_bg').css('display', 'none');
    });

    //편집팝업 aside
    $('#edit_aside > div').click(function() {
        $('#edit_aside > div i').css('display', 'none');
        $('#edit_aside > div').css({'background-color':'#FAFAFA','color': 'black','border-left':'none'});

        $(this).find('i').css('display', 'block');
        $(this).css({'background-color':'#EDEDED','color': '#1eb8ff','border-left':'1px solid #1eb8ff'});
    });

    //일반버튼
    $('#common_btn').click(function() {
        $('#common_outer').css('display', 'block');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
    });
    //시스템버튼
    $('#system_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'block');
        $('#host_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
    });
    //호스트버튼
    $('#host_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'block');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
    });
    //고가용성 버튼
    $('#ha_mode_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'block');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'none');
    });
    //리소스할당 버튼
    $('#res_alloc_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'block');
        $('#boot_outer').css('display', 'none');
    });
    //부트 옵션
    $('#boot_btn').click(function() {
        $('#common_outer').css('display', 'none');
        $('#system_outer').css('display', 'none');
        $('#host_outer').css('display', 'none');
        $('#ha_mode_outer').css('display', 'none');
        $('#res_alloc_outer').css('display', 'none');
        $('#boot_outer').css('display', 'block');
    });

     // footer 팝업창
     $('#footer>button').click(function(){
        if($('#footer_content').css('display') === 'none'){
            $('#footer_content').css('display', 'block');
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