$(function() {
    $('#header_left>i').click(function(){
        if($('#aside').css('display') === 'none'){
            $('#aside').css('display', 'block');
            $("#section").css('width','82%');
		} else {
			$('#aside').css('display', 'none');
            $("#section").css('width','100%');
		}
        
    });
    // 메뉴팝업창
    $('#popup_btn').click(function(){
        if($('#popup_box').css('display') === 'none'){
            $('#popup_box').css('display', 'block');
		} else {
			$('#popup_box').css('display', 'none');
		}
    });

    //aside item클릭
    $('.aside_item').click(function(){
        $('.aside_item').css({'border-left':'none'});
        if($(this).css('padding-left') <= '3.3vw' || $(this).css('padding-left') >= '3.3vw'){
            $(this).css({'border-left':'3px solid black'});
        }else{
            $(this).css({'border-left':'none','padding-left':'3.3vw'});
        }
    });

    /*item하나 클릭했을때*/
    $('.item').click(function(){
        $('.item:nth-child(even)').css('background-color','#ebebeb');
        $('.item:nth-child(odd)').css('background-color','white');
        if($(this).css('background-color') === 'rgb(255, 255, 255)'){
            $(this).css({'background-color':'#0273FA','color':'white'});
        } else if($(this).css('background-color') === 'rgb(235, 235, 235)'){
            $(this).css('background-color', '#0273FA');
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
    

    // 우클릭 드롭박스
      $(document).ready(function(){
        //Show contextmenu:
        $(document).contextmenu(function(e){
          //Get window size:
          var winWidth = $(document).width();
          var winHeight = $(document).height();
          //Get pointer position:
          var posX = e.pageX;
          var posY = e.pageY;
          //Get contextmenu size:
          var menuWidth = $(".contextmenu").width();
          var menuHeight = $(".contextmenu").height();
          //Security margin:
          var secMargin = 10;
          //Prevent page overflow:
          if(posX + menuWidth + secMargin >= winWidth
          && posY + menuHeight + secMargin >= winHeight){
            //Case 1: right-bottom overflow:
            posLeft = posX - menuWidth - secMargin + "px";
            posTop = posY - menuHeight - secMargin + "px";
          }
          else if(posX + menuWidth + secMargin >= winWidth){
            //Case 2: right overflow:
            posLeft = posX - menuWidth - secMargin + "px";
            posTop = posY + secMargin + "px";
          }
          else if(posY + menuHeight + secMargin >= winHeight){
            //Case 3: bottom overflow:
            posLeft = posX + secMargin + "px";
            posTop = posY - menuHeight - secMargin + "px";
          }
          else {
            //Case 4: default values:
            posLeft = posX + secMargin + "px";
            posTop = posY + secMargin + "px";
          };
          //Display contextmenu:
          $(".contextmenu").css({
            "left": posLeft,
            "top": posTop
          }).show();
          //Prevent browser default contextmenu.
          return false;
        });
        //Hide contextmenu:
        $(document).click(function(){
          $(".contextmenu").hide();
        });
      });
      
      	//반려동물 세부창
    $("#domain").click(function(){
      $("#animal_outer").css("display","block");
      $("#animal").css("display","block");
      $("body").css("overflow-y","hidden");
    });
    $("#animal_outer").click(function(){
      $("#animal_outer").css("display","none");
      $("#animal").css("display","none");
      $("body").css("overflow-y","scroll");
    });
    $(".animal_header>button").click(function(){
      $("#animal_outer").css("display","none");
      $("#animal").css("display","none");
      $("body").css("overflow-y","scroll");
    });
  
});