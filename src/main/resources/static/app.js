(() => {
    'use strict';

    const numberFormat = new Intl.NumberFormat('en-US');

    /** Fetches the vote count for one color and writes it into that row's votes cell. */
    async function loadVotes(link) {
        const cell = link.closest('tr').querySelector('.votes-cell');
        if (cell.getAttribute('aria-busy') === 'true') {
            return;
        }
        cell.setAttribute('aria-busy', 'true');
        cell.classList.remove('error');
        cell.textContent = 'Loading…';

        try {
            const response = await fetch(link.href, { headers: { Accept: 'application/json' } });
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}`);
            }
            const { votes } = await response.json();
            cell.dataset.votes = String(votes);
            cell.textContent = numberFormat.format(votes);
        } catch (err) {
            delete cell.dataset.votes;
            cell.classList.add('error');
            cell.textContent = 'Could not load votes';
            console.error('Failed to load votes for', link.textContent, err);
        } finally {
            cell.removeAttribute('aria-busy');
        }
    }

    /** Client-side only: sums the vote counts currently shown in the table. */
    function showTotal(table) {
        const total = [...table.querySelectorAll('tbody .votes-cell[data-votes]')]
            .reduce((sum, cell) => sum + Number(cell.dataset.votes), 0);
        document.getElementById('total-cell').textContent = numberFormat.format(total);
    }

    document.addEventListener('DOMContentLoaded', () => {
        const table = document.getElementById('votes-table');

        table.addEventListener('click', (event) => {
            const link = event.target.closest('a');
            if (!link) {
                return;
            }
            event.preventDefault();
            if (link.classList.contains('color-link')) {
                loadVotes(link);
            } else if (link.id === 'total-link') {
                showTotal(table);
            }
        });
    });
})();
