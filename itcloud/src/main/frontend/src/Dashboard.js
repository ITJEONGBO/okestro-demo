import React, { useEffect } from 'react';
import './App.css';
import {Routes, Route, BrowserRouter,Link} from 'react-router-dom';
import Machine from './Machine';



function Dashboard() {
  useEffect(() => {
    function adjustFontSize() {
      const width = window.innerWidth;
      const fontSize = width / 40; // 필요에 따라 이 값을 조정하세요
      document.documentElement.style.fontSize = fontSize + 'px';
    }

    // 창 크기가 변경될 때 adjustFontSize 함수 호출
    window.addEventListener('resize', adjustFontSize);

    // 컴포넌트가 마운트될 때 adjustFontSize 함수 호출
    adjustFontSize();

    // 컴포넌트가 언마운트될 때 이벤트 리스너 제거
    return () => {
      window.removeEventListener('resize', adjustFontSize);
    };
  }, []);



  return (
    <div id='body'>
      <div id="header">
        <div id="header_right">
          <span>Rutil Vm</span>
        </div>

        <div id="header_left">
          <div>
            <i className="fa fa-hdd-o"></i>
          </div>
          <div>
            <i className="fa fa-bell"></i>
          </div>
          <div>
            <i className="fa fa-user"></i>
            <span>(user name)</span>
          </div>
        </div>
      </div>

      <div id="main_outer">
        
        <div id="aside_outer">
          <div id="aside">
          
            <div id="nav">
              <div id="aside_popup_dashboard_btn">
                <i className="fa fa-th-large"></i>
              </div>
              <Link to='/machine'  className="link-no-underline">
                <div id="aside_popup_machine_btn">
                  <i className="fa fa-desktop"></i>
                </div>
              </Link>
              <div id="aside_popup_storage_btn">
                <i className="fa fa-server"></i>
              </div>
              <div id="aside_popup_network_btn">
                <i className="fa fa-database"></i>
              </div>
            </div>
            <div id="setting_icon">
              <i className="fa fa-cog"></i>
            </div>
              
            
          </div>{/* aside끝 */}

          <div id="aside_popup">


          </div>{/* aside_popup끝 */}
        </div>{/* aside_outer끝 */}

        {/* 대시보드 section */}
        <div id="dash_board">
          <div id="dash_boxs">
            <div className="box">
              <span>UPTIME</span>
              <h1>2</h1>
            </div>
            <div className="box">
              <span>데이터센터</span>
              <h1>2</h1>
              <div className="arrows">
                <i className="fa fa-arrow-up">1</i>
                <i className="fa fa-arrow-down">1</i>
              </div>
            </div>
            <div className="box">
              <span>클러스터</span>
              <h1>2</h1>
            </div>
            <div className="box">
              <span>호스트</span>
              <h1>2</h1>
              <div className="arrows">
                <i className="fa fa-arrow-up">1</i>
                <i className="fa fa-arrow-down">1</i>
              </div>
            </div>
            <div className="box">
              <span>데이터스토리지도메인</span>
              <h1>2</h1>
            </div>
            <div className="box">
              <span>가상머신</span>
              <h1>2</h1>
              <div className="arrows">
                <i className="fa fa-arrow-up">1</i>
                <i className="fa fa-arrow-down">1</i>
              </div>
            </div>
            <div className="box">
              <span>이벤트</span>
              <h1>0</h1>
              <div className="arrows">
                <i className="fa fa-arrow-up">1</i>
                <i className="fa fa-arrow-down">1</i>
              </div>
            </div>
          </div> {/* boxs 끝 */}

          <div id="dash_section">
            <div className="dash_section_contents"> 
              <h1>CPU</h1>
              <div className="graphs">
                <div className="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                  <div className="circle-graph1 circle-graph"> {/* 클래스명 수정 */}
                    <strong className="circle-percent"></strong>
                  </div>
                </div>
                <div><canvas id="bar-chart-horizontal"></canvas></div>
              </div>
              <span>USED 64 Core / Total 192 Core</span>
              <div className="wave_graph">
                <h2>Per Host</h2>
                <div>
                  <canvas id="line-chart" width="546" height="142"></canvas>
                </div>
              </div>
            </div>

            <div className="dash_section_contents">
              <h1>Memory</h1>
              <div className="graphs">
                <div className="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                  <div className="circle-graph2 circle-graph"> {/* 클래스명 수정 */}
                    <strong className="circle-percent"></strong>
                  </div>
                </div>
                <div><canvas id="bar-chart-horizontal2"></canvas></div>
              </div>
              <span>USED 64 Core / Total 192 Core</span>
              <div className="wave_graph">
                <h2>Per Host</h2>
                <div>
                  <canvas id="line-chart2" width="546" height="142"></canvas>
                </div>
              </div>
            </div>

            <div className="dash_section_contents" style={{ borderRight: 'none' }}>
              <h1>CPU</h1>
              <div className="graphs">
                <div className="graph-wrap active-on-visible" data-active-on-visible-callback-func-name="CircleRun">
                  <div className="circle-graph3 circle-graph"> {/* 클래스명 수정 */}
                    <strong className="circle-percent"></strong>
                  </div>
                </div>
                <div><canvas id="bar-chart-horizontal3"></canvas></div>
              </div>
              <span>USED 64 Core / Total 192 Core</span>
              <div className="wave_graph">
                <h2>Per Host</h2>
                <div>
                  <canvas id="line-chart3" width="546" height="142"></canvas>
                </div>
              </div>
            </div>
          </div> {/* dash section 끝 */}
        
          <div id="bar">
            <div>
              <span>CPU(시간 경과에 따른 CPU사용량)</span>
              <div>d</div>
            </div>
            <div>
              <span>MEMORY(시간 경과에 따른 Memory사용량)</span>
              <div>d</div>
            </div>
            <div>
              <span>Ethernet(시간 경과에 따른 Ethernet속도)</span>
              <div>d</div>
            </div>  
          </div> 
        
        </div> {/* 대시보드 section끝 */}


      </div>{/* main_outer끝 */}
    </div>
  );
}

export default Dashboard;
