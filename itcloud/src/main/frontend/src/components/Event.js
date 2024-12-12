import { faListUl } from '@fortawesome/free-solid-svg-icons';
import HeaderButton from './button/HeaderButton';
import Footer from './footer/Footer';
import { useAllEvents } from '../api/RQHook';
import EventTable from './table/EventTable';

// Modal.setAppElement('#root');

const Event = () => {
  const { 
    data: events, 
    status: eventsStatus,
    isRefetching: isEventsRefetching,
    refetch: refetchEvents, 
    isError: isEventsError, 
    error: eventsError, 
    isLoading: isEventsLoading,
  } = useAllEvents((e) => ({...e,}));
  

  return (
    <div id="section">
      <HeaderButton
        titleIcon={faListUl}
        title="Event"
        subtitle="Chart"
        buttons={[]}
        popupItems={[]}
        openModal={[]}
        togglePopup={() => {}}
      />
      <div className="content_outer">
        <div className="empty_nav_outer">
          <EventTable 
            events={events}
          />
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default Event;
