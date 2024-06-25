$(function() {    
    $('#section_header>div:nth-child(2)').click(function(){
        location.href = 'http://127.0.0.1:5500/Darkdashboard4.html';
    });
    $('table tr:nth-child(n+2)').click(function(){
        location.href = 'http://127.0.0.1:5500/Darkdashboard5.html';
    });
    $('#header_right > span').click(function(){
        location.href = 'http://127.0.0.1:5500/Darkdashboard.html';
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

});