import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import App from './App'

beforeEach(() => {
  global.fetch = jest.fn()
})

afterEach(() => {
  jest.restoreAllMocks()
})

test('renders todos loaded from the Spring backend', async () => {
  fetch.mockResolvedValueOnce({
    json: async () => [
      { taskdescription: 'Jest lernen' },
      { taskdescription: 'MockMvc testen' },
    ],
  })

  render(<App />)

  expect(await screen.findByText('Task 1: Jest lernen')).toBeInTheDocument()
  expect(screen.getByText('Task 2: MockMvc testen')).toBeInTheDocument()
  expect(fetch).toHaveBeenCalledWith('http://localhost:8080/')
})

test('submits a new todo to the tasks endpoint', async () => {
  fetch
    .mockResolvedValueOnce({ json: async () => [] })
    .mockResolvedValueOnce({})

  render(<App />)

  await userEvent.type(screen.getByRole('textbox'), 'Neue Aufgabe')
  await userEvent.click(screen.getByRole('button', { name: 'Absenden' }))

  await waitFor(() => {
    expect(fetch).toHaveBeenCalledWith('http://localhost:8080/tasks', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ taskdescription: 'Neue Aufgabe' }),
    })
  })
})
