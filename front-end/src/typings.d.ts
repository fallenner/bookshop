declare module '*.less' {
  const classes: Record<string, string>;
  export default classes;
}

declare module '*.png' {
  export default any;
}
