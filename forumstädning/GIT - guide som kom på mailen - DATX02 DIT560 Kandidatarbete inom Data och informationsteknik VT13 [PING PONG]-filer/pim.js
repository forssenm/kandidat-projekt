/** pim.js: Javascript functionality needed to check for pim. */

/** The function to send a http request to check for new unread messages. Called regularly. */
function pimSendQuery() {
    if (getCookie('pimConversation') === null) {
        var myRequest = new Ajax.Request('/pimCheckUnread.do', { 
            onSuccess: function(transport) {
                if (!getCookie('pimConversation') && transport.status == 200 && myRequest.getHeader('PimOk')) {
                    popupPim('/pimWritePopup.do?recipient=' + transport.responseText);
                }
            }
            , method: 'GET'
        });
    }
}

setInterval('pimSendQuery()', 180 * 1000);