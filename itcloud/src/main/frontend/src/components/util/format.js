/**
 * Converts bytes to gigabytes and formats the result to one decimal place.
 * @param {number} bytes - The number of bytes to convert.
 * @returns {string} The formatted size in GB.
 */
export function formatBytesToGB(bytes) {
  return (bytes / (1024 * 1024 * 1024)).toFixed(1) + " GB";
}